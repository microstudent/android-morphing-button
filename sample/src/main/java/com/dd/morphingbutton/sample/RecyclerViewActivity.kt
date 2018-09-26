package com.dd.morphingbutton.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dd.morphingbutton.impl.CircularProgressButton
import com.dd.morphingbutton.utils.ProgressGenerator

class RecyclerViewActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        recyclerView = findViewById(R.id.rv) as RecyclerView
        var adapter = MyAdapter()
        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
    }


    class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.view_item_cirprobutton, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
            val button = holder!!.button
            button.setState(CircularProgressButton.StateEnum.IDLE, false)
            button.setShowCenterIcon(true)
            button.setOnClickListener(View.OnClickListener {
                when (button.currentStateEnum) {
                    CircularProgressButton.StateEnum.IDLE -> {
                        val generator = ProgressGenerator(ProgressGenerator.OnCompleteListener {})
                        button.setState(CircularProgressButton.StateEnum.PROGRESS, true)
                        generator.start(button)
                    }
                    CircularProgressButton.StateEnum.PROGRESS -> {
                        button.setState(CircularProgressButton.StateEnum.TEXT, true)
                    }
                    CircularProgressButton.StateEnum.TEXT -> button.setState(CircularProgressButton.StateEnum.COMPLETE, true)
                    CircularProgressButton.StateEnum.COMPLETE -> button.setState(CircularProgressButton.StateEnum.IDLE, true)
                    else -> {
                    }
                }
            })
        }
    }

    class MyViewHolder : RecyclerView.ViewHolder {

        private var textView: View

        public var button: CircularProgressButton

        constructor(itemView: View?) : super(itemView) {
            textView = itemView!!.findViewById(R.id.tv)
            button = itemView.findViewById(R.id.bt) as CircularProgressButton
        }
    }
}
