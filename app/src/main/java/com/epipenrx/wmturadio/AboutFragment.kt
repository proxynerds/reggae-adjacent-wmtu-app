package com.epipenrx.wmturadio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.epipenrx.wmturadio.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wmtuLink.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.WMTU_WEBSITE_URL)))
        }
        binding.mtuLink.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.MTU_WEBSITE_URL)))
        }

        val context = requireContext()
        binding.proxyNerdsContactEmail.text = Constants.CONTACT_EMAIL
        binding.proxyNerdsContactEmail.contentDescription = getString(R.string.options_about_contact_cd)
        binding.proxyNerdsContactEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + Constants.CONTACT_EMAIL))
            startActivity(Intent.createChooser(intent, null))
        }
        binding.proxyNerdsPrivacyPolicyLink.setOnClickListener {
            startActivity(Intent(context, PrivacyPolicyActivity::class.java))
        }
        val versionName = runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }.getOrNull() ?: "?"
        binding.proxyNerdsVersionText.text = getString(R.string.options_about_version, versionName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
