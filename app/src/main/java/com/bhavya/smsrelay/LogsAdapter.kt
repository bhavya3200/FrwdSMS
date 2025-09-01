package com.bhavya.smsrelay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhavya.smsrelay.databinding.RowLogBinding
import org.json.JSONObject
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class LogEntry(val ts: Long, val from: String, val body: String, val result: String)

object LogStore {
    private const val FILE = "logs.jsonl"
    fun append(ctx: android.content.Context, e: LogEntry) {
        runCatching {
            File(ctx.filesDir, FILE).appendText(JSONObject()
                .put("ts", e.ts).put("from", e.from).put("body", e.body).put("result", e.result).toString()+"\n")
        }
    }
    fun readAll(ctx: android.content.Context): List<LogEntry> {
        val f = File(ctx.filesDir, FILE); if (!f.exists()) return emptyList()
        return f.readLines().mapNotNull { ln ->
            runCatching { val o=JSONObject(ln); LogEntry(o.getLong("ts"),o.getString("from"),o.getString("body"),o.getString("result")) }.getOrNull()
        }.asReversed()
    }
    fun fmt(ts: Long): String {
        val tz = ZoneId.systemDefault()
        val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return Instant.ofEpochMilli(ts).atZone(tz).format(fmt)
    }
}

class LogsAdapter : RecyclerView.Adapter<LogsAdapter.VH>() {
    private var items: List<LogEntry> = emptyList()
    fun submit(data: List<LogEntry>) { items = data; notifyDataSetChanged() }

    class VH(val b: RowLogBinding) : RecyclerView.ViewHolder(b.root)
    override fun onCreateViewHolder(p: ViewGroup, v: Int): VH =
        VH(RowLogBinding.inflate(LayoutInflater.from(p.context), p, false))

    override fun onBindViewHolder(h: VH, pos: Int) {
        val e = items[pos]
        h.b.tvWhen.text = LogStore.fmt(e.ts)
        h.b.tvFrom.text = "From: ${e.from}"
        h.b.tvBody.text = e.body
        h.b.tvResult.text = e.result
    }
    override fun getItemCount() = items.size
}