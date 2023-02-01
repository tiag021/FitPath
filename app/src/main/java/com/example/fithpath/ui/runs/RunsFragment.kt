package com.example.fithpath.ui.runs

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import com.example.fithpath.R
import com.example.fithpath.databinding.FragmentRunsBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


/**
 * A fragment representing a list of Items.
 */
class RunsFragment : Fragment() {


    private lateinit var runs: ExpandableListView
    private lateinit var arrayAdapter: ExpandableListViewAdapter
    private var _binding: FragmentRunsBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_runs, container, false)

        runs = rootView.findViewById(R.id.elView)
        //leitura do internal Storage
        val directory: File = activity!!.filesDir
        val file: File = File(directory, "runsData.txt")
        val content = ByteArray(file.length().toInt())

        if (file.isFile) {
            try {
                //obtem o ficheiro de dados sobre as corridas
                val fi: FileInputStream = FileInputStream(file)
                fi.read(content)
                fi.close()

                //guardar o content numa string
                var s: String = String(content)
                //retirar os []
                s = s.substring(1, s.length - 1)
                //separar os valores
                val values = s.split("][")
                //guarda-los num array
                val statsList = ArrayList(values)

                //obtem as fotos
                val photoList: HashMap<String, String> = HashMap()
                val path = (Environment.getExternalStorageDirectory().toString())
                val appDirectory = File("$path/DCIM/FitPath").listFiles()
                val appDirectoryFiles = arrayListOf<String>()

                if (appDirectory != null) {
                    for (file in appDirectory) {
                        appDirectoryFiles.add(file.path)
                    }
                }


                for(i in 0 until statsList.size){
                    photoList.put(statsList[i], appDirectoryFiles[i])
                }
                //mostra aos valores na listview

                arrayAdapter =
                    ExpandableListViewAdapter(activity!!, statsList, photoList)
                runs.setAdapter(arrayAdapter)
            } catch (e: FileNotFoundException) {

                runs.setOnClickListener {

                }
            }
        }
        return rootView
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}