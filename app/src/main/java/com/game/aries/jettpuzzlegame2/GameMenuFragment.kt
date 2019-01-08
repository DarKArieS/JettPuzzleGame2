package com.game.aries.jettpuzzlegame2

import com.game.aries.jettpuzzlegame2.models.*

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import kotlinx.android.synthetic.main.fragment_game_menu.view.*

class GameMenuFragment : Fragment() {
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_game_menu, container, false)
        // Set Views
        rootView.goGameMenuButton.setOnClickListener {if(!MainModel.checkIsLoading())clickGoGameMenuButton()}

        return rootView
    }

    private fun clickGoGameMenuButton(){
        val transition = Transition()
        transition.navigation = {
            (activity as MainActivity).findNavController(R.id.fragmentHost)
                .navigate(GameMenuFragmentDirections.actionGameMenuFragmentToGameFragment())

        }
        transition.communication={callback -> MainModel.fakeCommunicate(callback)}
        transition.commit((activity as MainActivity))
    }

}
