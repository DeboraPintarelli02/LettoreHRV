package com.example.watch.data

class MemoraHR {
    private val valori = ArrayList<HR>()

    companion object{
        private var instance: MemoraHR? = null

        @Synchronized
        fun getInstance(): MemoraHR{
            if(instance == null)
                instance = MemoraHR()

            return instance!!
        }
    }

    private constructor();


    @Synchronized
    fun registraValore(hr: HR){
        valori.add(hr)
    }

    @Synchronized
    fun pulisciValori(){
        valori.clear()
    }

    @Synchronized
    fun ottieniValori(): ArrayList<HR>{
        return ArrayList(valori)
    }
}