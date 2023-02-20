import SearchHelper from '../../searchHelper'
import StringManipulationUtils from './../../../../mixins/StringManipulationUtils'

/**
 * Here we keep all the specific logic and options
 * needed for configuring the chart
 * 
 */
export default {
  /**
  * Generate labels for chart.
  * 
  */
  getChartLabels: (labels, scale) => {
    let labelsFormated = []
    for (const label of labels) {
      labelsFormated.push(StringManipulationUtils.methods.$_displayDate(label, scale))
    }
    return labelsFormated
  },

  /**
  * Generate options for chart.
  * 
  */
  getChartOptions(searchType, scale) {
    return {
     tooltips:this.getTooltipOptions(),
     scales: this.getScalesOptions(scale),
     //responsive: true,
     maintainAspectRatio: true,
     onClick: (evt, chartObj) => {
       this.getChartPointCallback(evt, chartObj, searchType, scale)
    }
   }
  },

  /**
   * The callback attached to all clickable points on
   * the chart. 
   * 
   * Generates and executes a search (new tab) when user
   * clicks a a point    
   */
  getChartPointCallback(evt, chartObj, searchType, scale) {
    // We have to fetch the chart instance this way because direct 
    // access to the vue chart instance is out of scope here (resides LineChart.js).
    // If you try to go the "correct way" and enrich options on render in LineChart.js
    // vueChartJS refuses to function correctly on first search
    if (chartObj.length > 0) {
    const chartInstance = chartObj[0]._chart
    const activeElement = chartInstance.getElementAtEvent(evt)
    if (activeElement.length > 0) {
      const dateFromClick = chartInstance.config.data.rawLabels[activeElement[0]._index]
      const queryFromClick = chartInstance.config.data.datasets[activeElement[0]._datasetIndex].label
      SearchHelper.handleSearch(queryFromClick, dateFromClick, searchType, scale)
    }
  }
  },

  /**
  * Generate dataset for chart.
  * 
  */
  getChartDataSet(rawData) {
    let datasetsEnrichedWithConfig = []
    rawData.forEach((val, i) => {
      const datasetWithConfig = Object.assign(
        {
          label: val.query,
          data: val.percent,
          data_abs: { count: val.count, total: val.total },
          date: val.label
        },
        this.getChartVisualDataPointConfig(i),
      )
      datasetsEnrichedWithConfig.push(datasetWithConfig)
    })
    
    return datasetsEnrichedWithConfig
  },

  /**
  * Line and data point config for chart
  * 
  */
  getChartVisualDataPointConfig(i) {
    return {
      borderColor: this.getChartLineColor(i),
      backgroundColor: this.getChartLineColor(i),
      tension: 0.4,
      fill: false
    }
  },

  getScalesOptions(scale='') {
    return {
      yAxes: [{
        scaleLabel: {
          display: true,
          labelString: 'No of hits in %'
        },
        ticks: { min: 0 }
      }],
      xAxes: [{
        scaleLabel: {
          display: true,
          labelString: 'Time in ' + scale.toLowerCase() + 's'
        }
      }]
    }
  },

  /**
  * Generate random color for lines
  * Kudos - https://stackoverflow.com/questions/10014271/generate-random-color-distinguishable-to-humans 
  */
  getChartLineColor(salt){
    const hue = salt * 137.508 // use golden angle approximation
    return `hsl(${hue},50%,75%)`
  },



  /** 
   * Tooltip options
   * 
  */

  //Tooltip option base
  getTooltipOptions(){
    return {
      mode: 'index',
      titleSpacing: 20,
      titleFontSize: 13,
      bodyFontSize: 12,
      yPadding: 20,
      xPadding: 20,
      bodySpacing: 10,
      titleMarginBottom: 20,
      footerMarginTop: 20,
      multiKeyBackground: 'rgba(0,0,0,0.8)',
      callbacks:this.getTooltipCallbacks() 
    }    
  },

  //Setup for Tooltip options callbacks for title, label and labelColor
  getTooltipCallbacks() {
      //Setting up custom tooltips
      return {
        
        title: (tooltipItems) => {
           return this.getTitleCallback(tooltipItems)
        },

        label: (tooltipItem, data) => {
            return this.getLabelCallback(tooltipItem, data)
       },
        
        labelColor: (tooltipItem, chartInstance) => {
           return this.getLabelColorCallback(tooltipItem, chartInstance)
            
          }
        }
      },

  //Tooltip options callbacks label
  getLabelCallback(tooltipItem, data) {
        let frac = 0.0000
        let labeltext = data.datasets[tooltipItem.datasetIndex].label || ''
        labeltext = labeltext.length > 50 ? labeltext.substring(0, 50) + ' (...)' : labeltext
        if (tooltipItem.yLabel > 0) {
             const currentDatasetItem = data.datasets[tooltipItem.datasetIndex]
             const labelCount = currentDatasetItem .data_abs.count[tooltipItem.index] || ''
             const labelTotalCount = currentDatasetItem .data_abs.total[tooltipItem.index] || ''
             frac = tooltipItem.yLabel
             labeltext = `${labeltext}: ${frac}% (${labelCount}/${labelTotalCount} hits)`
         } else {
             labeltext = `${labeltext}: ${frac}%`
         }
          return labeltext
  },

  //Tooltip options callbacks title
  getTitleCallback(tooltipItems){
    // Pick first xLabel for now
    return  `Date: ${tooltipItems[0].xLabel}`
  },

  //Tooltip options callback for label color
  getLabelColorCallback(tooltipItem, chartInstance) {
    return {
        borderColor: 'transparent',
        backgroundColor: chartInstance.config.data.datasets[tooltipItem.datasetIndex].borderColor
    }
  }

    


  
}