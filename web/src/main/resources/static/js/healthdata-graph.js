function drawChart(selector, ajaxUrl, bindto, dateSelector, message, chartFunction) {
    const terms = $("input[name=" + selector + "]:checked").val();
    displayDate(terms, dateSelector);

    $.ajax({
        url: ajaxUrl,
        type: 'get',
        data: {
            term: terms,
            timezone: moment.tz.guess()
        },
        dataType: 'json'
    }).done(function(retrieveData, textStatus, jqXHR) {

        const chartElm = document.getElementById(bindto);

        if (typeof retrieveData[0] === "undefined") {
            $(message).text('該当データがありません');
            chartElm.style.display = "none";
        } else {
            $(message).text('');
            chartElm.style.display = "block";
            const ctx = chartElm.getContext('2d');

            // construct data for XAxes
            const xAxesData =
                retrieveData.map(d => {
                    return moment(d.time).tz(moment.tz.guess()).format("YYYY/MM/DD HH:mm");
                });

            chartFunction(ctx, retrieveData, xAxesData);
        }

    }).fail(function(jqXHR, textStatus, errorThrown) {
        console.log(jqXHR.status);
        console.log(textStatus);
        console.log(errorThrown);
    });

}


function getBodyTemperatureWithTerm (selector, ajaxUrl, bindto, dateSelector, message) {
    const createBodyTemperature = (ctx, retrieveData, xAxesData) => {
        const constructedYData =
            retrieveData.map(d => {
                return d.bodyTemperature;
            });

        // construct data for threshold line
        const thresholdData =
            retrieveData.map(d => {
                return 37.5;
            });

        const bodyTemperatureCoreUiChart =
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: xAxesData,
                    datasets: [
                        {
                            label: "体温",
                            borderColor: '#321fdb',
                            data: constructedYData
                        },
                        {
                            label: "閾値",
                            borderColor: '#FF0000',
                            pointRadius: 0,
                            data: thresholdData
                        }
                    ]
                },
                options: {
                    scales: {
                        yAxes: [{
                            ticks: {
                                min: 35.0,
                                max: 40.0
                            },
                            scaleLabel: {
                                display: true,
                                labelString: '℃'
                            }
                        }],
                        xAxes: [{
                            type: 'time',
                            time: {
                                unit: 'day',
                            }
                        }]
                    }
                }
            });
    }
    drawChart(selector, ajaxUrl, bindto, dateSelector, message, createBodyTemperature);
}

function getBloodPressureWithTerm (selector, ajaxUrl, bindto, dateSelector, message) {
    const createBloodPressureChart = (ctx, retrieveData, xAxesData) => {
        var diastolicBloodPressure =
            retrieveData.map(d => {
                return d.diastolicBloodPressure;
            });
        var systolicBloodPressure =
            retrieveData.map(d => {
                return d.systolicBloodPressure;
            });

        var bloodPressureChart =
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: xAxesData,
                    datasets: [
                        {
                            label: "拡張期血圧",
                            borderColor: '#FF0000',
                            data: diastolicBloodPressure
                        },
                        {
                            label: "収縮期血圧",
                            borderColor: '#321fdb',
                            data: systolicBloodPressure
                        }
                    ]
                },
                options: {
                    scales: {
                        yAxes: [{
                            scaleLabel: {
                                display: true,
                                labelString: 'mmHg'
                            }
                        }],
                        xAxes: [{
                            type: 'time',
                            time: {
                                unit: 'day'
                            }
                        }]
                    }
                }
            });
    };
    drawChart(selector, ajaxUrl, bindto, dateSelector, message, createBloodPressureChart);
}

function getPulseRateWithTerm (selector, ajaxUrl, bindto, dateSelector, message) {
    const createPulseRateChart = (ctx, retrieveData, xAxesData) => {
        var constructedYData =
            retrieveData.map(d => {
                return d.pulseRate;
            });

        var pulseRateChart =
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: xAxesData,
                    datasets: [
                        {
                            label: "脈拍",
                            borderColor: '#321fdb',
                            data: constructedYData
                        }
                    ]
                },
                options: {
                    scales: {
                        yAxes: [{
                            scaleLabel: {
                                display: true,
                                labelString: '回'
                            }
                        }],
                        xAxes: [{
                            type: 'time',
                            time: {
                                unit: 'day',
                            }
                        }]
                    }
                }
            });
    };
    drawChart(selector, ajaxUrl, bindto, dateSelector, message, createPulseRateChart);
}

function getSpO2WithTerm (selector, ajaxUrl, bindto, dateSelector, message) {
    const createSpO2Chart = (ctx, retrieveData, xAxesData) => {
        var constructedYData =
            retrieveData.map(d => {
                return d.spO2;
            });

        var spO2Chart =
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: xAxesData,
                    datasets: [
                        {
                            label: "spO2",
                            borderColor: '#321fdb',
                            data: constructedYData
                        }
                    ]
                },
                options: {
                    scales: {
                        yAxes: [{
                            ticks: {
                                min: 85,
                                max: 100
                            },
                            scaleLabel: {
                                display: true,
                                labelString: '%'
                            }
                        }],
                        xAxes: [{
                            type: 'time',
                            time: {
                                unit: 'day',
                            }
                        }]
                    }
                }
            });
    };
    drawChart(selector, ajaxUrl, bindto, dateSelector, message, createSpO2Chart);
}

function displayDate(termStr, dateSelector) {
  var today =  moment(new Date(), "YYYY/MM/DD");
  var todayStr = today.format("YYYY/MM/DD");
  if ('week' === termStr) {
    var pastDayStr = today.add(-7, 'days').format("YYYY/MM/DD")
  } else if ('twoweek' === termStr) {
    var pastDayStr = today.add(-14, 'days').format("YYYY/MM/DD")
  } else {
    // Nothing to do
  }

  if (typeof pastDayStr === 'undefined') {
    $(dateSelector).text(todayStr)
  } else {
    $(dateSelector).text(pastDayStr + " ~ " + todayStr)
  }

}

