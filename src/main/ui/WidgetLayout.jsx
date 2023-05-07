import React, { useState } from "react";
import { Responsive, WidthProvider } from "react-grid-layout";
import ReactWeather, { useOpenWeather } from 'react-open-weather';
import "react-grid-layout/css/styles.css";
import "react-resizable/css/styles.css";
import "./Dashboard.css";


const ResponsiveReactGridLayout = WidthProvider(Responsive);

const WidgetLayout = () => {
  const [layouts, setLayouts] = useState(null);
  const [widgetArray, setWidgetArray] = useState([
    { i: "time", x: 0, y: 0, w: 2, h: 2 },
    { i: "weather", x: 2, y: 2, w: 2, h: 2 },
  ]);

  const { data, isLoading, errorMessage } = useOpenWeather({
    key: '81b4120979184656cda86aba07b9bf83',
    lat: '-41.287',
    lon: '174.776',
    lang: 'en',
    unit: 'metric', // values are (metric, standard, imperial)
  });

  const generateWidgetContent = (i) => {
    console.log(i);
    switch (i) {
      case "time":
        return <div>Some widget here</div>;
      case "weather":
        return <ReactWeather
          isLoading={isLoading}
          errorMessage={errorMessage}
          data={data}
          lang="en"
          locationLabel="Wellington"
          unitsLabels={{ temperature: 'C', windSpeed: 'Km/h' }}
          showForecast
        />;
      default:
        return <div>New widget</div>;
    }
  };


  const generateWidgets = () => {
    return widgetArray?.map((widget, index) => {
      return (
        <div
          className="widget"
          key={index}
          data-grid={{
            x: widget?.x,
            y: widget?.y,
            w: widget?.w,
            h: widget?.h,
            i: widget.i,
            minW: 1,
            minH: 1,
            maxW: Infinity,
            maxH: Infinity,
            isDraggable: true,
            isResizable: true,
          }}
        >
          <button
            className="deleteButton"
            onClick={() => handleDelete(widget.i)}
          >
            x
          </button>
          <div className="widget-container">
            {generateWidgetContent(widget.i)}
          </div>
        </div>
      );
    })
  }

  const handleModify = (layouts, layout) => {
    const tempArray = widgetArray;
    setLayouts(layout);
    layouts?.map((position) => {
      tempArray[Number(position.i)].x = position.x;
      tempArray[Number(position.i)].y = position.y;
      tempArray[Number(position.i)].width = position.w;
      tempArray[Number(position.i)].height = position.h;
    });
    setWidgetArray(tempArray);
  };

  const handleAdd = () => {
    setWidgetArray([
      ...widgetArray,
      { i: "widget" + (widgetArray.length + 1), x: 0, y: 0, w: 2, h: 2 },
    ]);
  };

  const handleDelete = (key) => {
    const tempArray = widgetArray.slice();
    const index = tempArray.indexOf(tempArray.find((data) => data.i === key));
    tempArray.splice(index, 1);
    setWidgetArray(tempArray);
  };

  const handleApiKey = () => {
    const val = document.getElementById("apiKeyInput").value;
    document.cookie = "x-api-key=" + val;
  }

  return (
    <div>
      <button onClick={() => handleAdd()}>Add Widget</button>
      <input type="password" id="apiKeyInput" placeholder="API Key" />
      <button onClick={() => handleApiKey()}>Save</button>

      <ResponsiveReactGridLayout
        onLayoutChange={handleModify}
        verticalCompact={true}
        layout={layouts}
        breakpoints={{
          xxl: 1400, lg: 1200, md: 996, sm: 768, xs: 480, xxs: 0
        }}
        preventCollision={false}
        cols={{ xxl: 10, lg: 8, md: 8, sm: 4, xs: 2, xxs: 2 }}
        autoSize={true}
        margin={{
          xxl: [20, 20],
          lg: [20, 20],
          md: [20, 20],
          sm: [20, 20],
          xs: [20, 20],
          xxs: [20, 20],
        }}
      >
        {generateWidgets()}
      </ResponsiveReactGridLayout>
    </div>
  );
};

export default WidgetLayout;
