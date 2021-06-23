package pro.retor.simpledimple.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiConsumer
import io.reactivex.schedulers.Schedulers
import pro.retor.simpledimple.api.Api
import pro.retor.simpledimple.databinding.FragmentListBinding
import pro.retor.simpledimple.items.HeaderItem
import pro.retor.simpledimple.items.Item

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private var subscription: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        loadData()
    }

    //Move method to another architecture layer
    private fun loadData() {
        subscription?.let {
            if (!it.isDisposed) it.dispose()
        }
        subscription = Api.API.loadPersons()
            .flatMap { list ->
                Observable.fromIterable(list)
                    .map { Api.mapPerson(it) }
                    .toList()
                    .map {
                        val out: MutableList<Item> = ArrayList()
                        val listCount = it.count()
                        out.add(0, HeaderItem(listCount.toString()))
                        out.addAll(it)
                        out
                    }.subscribeOn(Schedulers.newThread())
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(fill())
    }

    private fun fill(): BiConsumer<List<Item>, Throwable> {
        return BiConsumer { list, error ->
            if (error != null) {
                showError(error.localizedMessage ?: "Something went wrong")
            } else {
                binding.list.adapter = PersonsAdapter(list)
            }
        }
    }

    private fun showError(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Ok") { dialog, _ -> dialog?.dismiss() }
            .show()
    }

    override fun onDestroyView() {
        subscription?.dispose()
        subscription = null
        super.onDestroyView()
        _binding = null
    }
}