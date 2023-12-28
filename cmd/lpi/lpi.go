package main

import (
	"flag"
	"fmt"
	"io"
	"net/http"
	"os"
	"time"

	"github.com/sirupsen/logrus"
)

var log = logrus.New()

/**
 * This simple CLI interacts with the LPI from the terminal.
 * It takes any number of parameters and makes a web request to the LPI,
 * returning the response.
 */
func main() {
	// Parse the command line flags
	var host string
	flag.StringVar(&host, "h", "", "LPI server")
	verbose := flag.Bool("v", false, "Verbose output")
	flag.Parse()
	// Set up logging
	log.Out = os.Stdout
	log.Level = logrus.InfoLevel
	if *verbose {
		log.Level = logrus.DebugLevel
	}
	// Parse the command line arguments
	var module string
	var command string
	var params []string
	args := flag.Args()
	if len(args) > 0 {
		module = args[0]
	}
	if len(args) > 1 {
		command = args[1]
	}
	if len(args) > 2 {
		params = args[2:]
	}
	// Get auth token
	token, present := os.LookupEnv("LPI_TOKEN")
	if !present {
		log.Fatal("LPI_TOKEN environment variable not set")
	}
	// Get LPI URL
	if host == "" {
		host, present = os.LookupEnv("LPI_URL")
		if !present {
			log.Fatal("LPI_URL environment variable not set")
		}
	}
	// Make a request to the LPI
	uri := fmt.Sprintf("http://%s/api", host)
	if module != "" {
		uri = fmt.Sprintf("%s/%s", uri, module)
	}
	if command != "" {
		uri = fmt.Sprintf("%s/%s", uri, command)
	}
	log.Debugf("Requesting %s", uri)
	request, err := http.NewRequest("GET", uri, nil)
	if err != nil {
		log.Fatal(err)
	}
	// Build request
	queries := request.URL.Query()
	for i, param := range params {
		log.Debugf("Param %d: %s", i, param)
		queries.Add(fmt.Sprint(i), param)
	}
	request.URL.RawQuery = queries.Encode()
	request.Header.Add("x-api-key", token)
	log.Debugf("x-api-key: %s", request.Header.Get("x-api-key"))
	client := &http.Client{
		Timeout: time.Second * 5,
	}
	// Send request
	response, err := client.Do(request)
	if err != nil {
		if os.IsTimeout(err) {
			log.Fatal("LPI did not respond after 5 seconds")
		}
		log.Fatal(err)
	}
	defer response.Body.Close()
	body, err := io.ReadAll(response.Body)
	if err != nil {
		log.Fatal(err)
	}
	// Print the response
	log.Debug(response.Status)
	if len(body) == 0 {
		os.Exit(0)
	}
	fmt.Println(string(body))
}
