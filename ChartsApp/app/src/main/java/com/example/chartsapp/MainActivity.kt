package com.example.chartsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.chartsapp.ui.theme.ChartsAppTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

const val steps = 10

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//             val coroutineScope = rememberCoroutineScope()
            val pointsList = getPointsList()
            val lineChartData = getLineChartData(pointsList)
            ChartsAppTheme {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BuildLineChart(lineChartData)
//                    BuildPointColumn(weatherModel)
                }

            }
        }
    }
    @Composable
    private fun BuildLineChart(lineChartData: LineChartData) {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            lineChartData = lineChartData
        )
    }


    private fun iniAxisData(pointsList: List<Point>): Map<AxisData, AxisData> {
        val xAxisData = AxisData.Builder()
            .axisStepSize(100.dp)
            .backgroundColor(Color.Transparent)
            .steps(pointsList.size - 1)
            .labelData { i -> i.toString() }
            .labelAndAxisLinePadding(15.dp)
            .build()

        val yAxisData = AxisData.Builder()
            .steps(steps)
            .backgroundColor(Color.Transparent)
            .labelAndAxisLinePadding(20.dp)
            .labelData { i ->
                val yScale = (90 - 50) / steps
                ((i * yScale) + 50).toString()
            }.build()

        return mapOf(xAxisData to yAxisData)
    }

    private fun getLineChartData(dataPoints: List<Point>): LineChartData {
        val xAxisData = iniAxisData(pointsList = dataPoints).keys.first()
        val yAxisData = iniAxisData(pointsList = dataPoints).values.first()
        return LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = dataPoints,
                        LineStyle(color = Color.Green, width = 1.0f),
                        IntersectionPoint(color = Color.Blue),
                        SelectionHighlightPoint(),
                        ShadowUnderLine(),
                        SelectionHighlightPopUp()
                    )
                ),
            ),
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            gridLines = GridLines(),
            backgroundColor = Color.White
        )
    }

    private fun getPointsList(): List<Point> {
        val list = ArrayList<Point>()
        for (i in 0..31) {
            list.add(
                Point(
                    i.toFloat(),
                    Random.nextInt(50, 90).toFloat()
                )
            )
        }
        return list
    }

//    private fun getFlowPoints(): Flow<List<Point>> = flow {
//        val list = ArrayList<Point>()
//        for (i in 0..31) {
//            delay(100L)
//            list.add(
//                Point(
//                    i.toFloat(),
//                    Random.nextInt(50, 90).toFloat()
//                )
//            )
//
//        }
//        emit(list)
//    }

//    private suspend fun listenFlow(
//        state: MutableState<List<Point>>,
//    ) {
//        state.value = getFlowPoints().toList().first()
//    }


//    private fun getFlowPoint(): Flow<Point> = flow {
//        for (i in 0..31) {
//            delay(1000L)
//            Log.d("MyLog", "NUM:$i")
//            val point = Point(
//                i.toFloat(),
//                Random.nextInt(50, 90).toFloat()
//            )
//            emit(point)
//        }
//    }

//    private suspend fun listenFlowPoint(
//        state: MutableState<List<Point>>,
//    ) {
//        val list = ArrayList<Point>()
//
//        getFlowPoint().collect {
//            list.add(it)
//            Log.d("MyLog", "Поток: ${Thread.currentThread().name}")
//            state.value = list.toList()
//        }
////        getFlowPoint().produceIn(GlobalScope).receiveAsFlow().collect{
////            list.add(it)
////                state.value = list.toList()
////        } collectAsState(initial = emptyList())
//
//
//    }


    //    @Composable
//    private fun BuildPointColumn(points: MutableState<List<Point>>) {
////        val incomePoint =  getFlowPoints().collectAsState(initial = emptyList()).value
//        LazyColumn {
//            items(points.value) { item ->
//                Box(
//                    modifier = Modifier
//                        .clickable {
//
//                        }
//                        .fillMaxWidth()
//                        .background(color = Color.LightGray)
//                        .padding(10.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(text = item.x.toString())
//                }
//
//            }
//
//        }
//    }

    //                val weatherModel = remember {
//                    mutableStateOf(emptyList<Point>())
//                }
//                LaunchedEffect(Unit) {
//                    coroutineScope.launch  {
//                        withContext(Dispatchers.Default) {
//                            val pointsFlow =  listenFlowPoint(weatherModel)
//                            Log.d("MyLog","Поток: ${Thread.currentThread().name}")
//                            Log.d("MyLog", "$pointsFlow")
//                        }
//                    }
//                }
}

