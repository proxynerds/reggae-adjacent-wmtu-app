package com.epipenrx.wmturadio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.epipenrx.wmturadio.databinding.FragmentOptionsBinding
import kotlin.math.roundToInt

class OptionsFragment : Fragment() {

    private var _binding: FragmentOptionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()

        when (Prefs.resumeMode(context)) {
            ResumeMode.CONTINUE -> binding.rbContinue.isChecked = true
            ResumeMode.RELOAD -> binding.rbReload.isChecked = true
        }
        binding.resumeModeGroup.setOnCheckedChangeListener { _, checkedId ->
            val mode = if (checkedId == binding.rbReload.id) ResumeMode.RELOAD else ResumeMode.CONTINUE
            Prefs.setResumeMode(context, mode)
        }

        val cacheMinutes = Prefs.cacheMinutes(context)
        binding.cacheMinutesSlider.valueFrom = Prefs.MIN_CACHE_MINUTES.toFloat()
        binding.cacheMinutesSlider.valueTo = Prefs.MAX_CACHE_MINUTES.toFloat()
        binding.cacheMinutesSlider.value = cacheMinutes.toFloat()
        updateCacheValueLabel(cacheMinutes)
        binding.cacheMinutesSlider.addOnChangeListener { _, value, fromUser ->
            val minutes = value.toInt()
            updateCacheValueLabel(minutes)
            if (fromUser) Prefs.setCacheMinutes(context, minutes)
        }

        binding.contactEmail.text = Constants.CONTACT_EMAIL
        binding.contactEmail.contentDescription = getString(R.string.options_about_contact_cd)
        binding.contactEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + Constants.CONTACT_EMAIL))
            startActivity(Intent.createChooser(intent, null))
        }

        binding.privacyPolicyLink.setOnClickListener {
            startActivity(Intent(context, PrivacyPolicyActivity::class.java))
        }

        val versionName = runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }.getOrNull() ?: "?"
        binding.versionText.text = getString(R.string.options_about_version, versionName)
    }

    private fun updateCacheValueLabel(minutes: Int) {
        binding.cacheValueLabel.text = resources.getQuantityString(
            R.plurals.options_buffer_value,
            minutes,
            minutes,
            formatApproxMemory(minutes)
        )
    }

    /** Rough estimate only -- see Constants.STREAM_APPROX_BYTES_PER_MINUTE. */
    private fun formatApproxMemory(minutes: Int): String {
        val megabytes = minutes * Constants.STREAM_APPROX_BYTES_PER_MINUTE / 1_000_000.0
        return if (megabytes < 10) {
            String.format("%.1f MB", megabytes)
        } else {
            "${megabytes.roundToInt()} MB"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
