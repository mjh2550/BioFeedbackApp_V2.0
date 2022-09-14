package com.android.tne.util

class CalcUtils {

    /**
     * 2022.07.18
     * Moving Average (Int)
     */
    fun movingAverage(totalScore : Int,provideList : ArrayList<Int>) : ArrayList<Int>{
        val m: Int = totalScore
        val list: ArrayList<Int> = provideList
        val n = list.size
        val resultList = ArrayList<Int>()

        var sum = 0

        for (i in 0 until m - 1) {
            sum += list[i] //m-1까지의 부분sum
        }

        for (i in m - 1 until n) {
            sum += list[i]
            resultList.add(sum / m)
            sum -= list[i - m + 1]
        }

        for(i in resultList){
            println("average is $i")
        }
        return  resultList
    }

}