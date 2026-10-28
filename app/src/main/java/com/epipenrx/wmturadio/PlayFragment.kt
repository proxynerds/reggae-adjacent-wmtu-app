package com.epipenrx.wmturadio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.Player
import com.epipenrx.wmturadio.databinding.FragmentPlayBinding

class PlayFragment : Fragment() {

    private var _binding: FragmentPlayBinding? = null
    private val binding get() = _binding!!
    private val host get() = requireActivity() as PlaybackHost

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updatePlayPauseUi(isPlaying)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.showReminderText.text = getString(
            R.string.show_reminder_summary,
            Constants.SHOW_NAME
        )

        binding.playPauseButton.setOnClickListener {
            val controller = host.currentController() ?: return@setOnClickListener
            if (controller.isPlaying) controller.pause() else host.playRespectingPreference()
        }

        binding.swipeRefresh.setColorSchemeResources(R.color.wmtu_gold)
        binding.swipeRefresh.setOnRefreshListener {
            host.reloadStream {
                binding.swipeRefresh.isRefreshing = false
            }
        }

        binding.wmtuWebsiteLink.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.WMTU_WEBSITE_URL)))
        }
        binding.wmtuArchivesLink.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.WMTU_ARCHIVES_URL)))
        }

        host.addPlaybackListener(playerListener)
        host.onControllerReady { c -> updatePlayPauseUi(c.isPlaying) }
    }

    override fun onDestroyView() {
        host.removePlaybackListener(playerListener)
        super.onDestroyView()
        _binding = null
    }

    private fun updatePlayPauseUi(isPlaying: Boolean) {
        binding.playPauseButton.setImageResource(
            if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play_arrow
        )
        binding.playPauseButton.contentDescription = getString(
            if (isPlaying) R.string.action_pause else R.string.action_play
        )
        binding.statusText.text = getString(
            if (isPlaying) R.string.status_playing else R.string.status_idle
        )
    }
}
