package com.example.Android_Task.ui.home


import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.*
import android.content.ContentValues.TAG
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.Android_Task.R
import com.example.Android_Task.ui.home.player.OnSwipeListener
import com.example.Android_Task.ui.home.player.mediaPlayerImplementation
import com.example.Android_Task.utilities.InjectorUtils
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.InternalCoroutinesApi
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast
import com.example.Android_Task.activities.VideoTrim
import android.media.MediaPlayer;
import java.io.File

@InternalCoroutinesApi
class HomeFragment : Fragment(), View.OnTouchListener {

    private val homeViewModel: HomeViewModel by viewModels {
        InjectorUtils.provideHomeViewModelFactory()
    }
    private lateinit var playerView: PlayerView
    private lateinit var likeButton: ToggleButton
    private lateinit var shareButton: ImageView
    private lateinit var retryLayout: LinearLayout
    private lateinit var retryTextView: TextView
    private lateinit var loading: ImageView

    private lateinit var listOfUrl: List<String>
    private lateinit var listOfDownloadUrl: List<String>
    private lateinit var mediaPlayer: mediaPlayerImplementation
    private lateinit var gestureDetector: GestureDetector
    private lateinit var downloadManger:DownloadManager
    private  lateinit var  progressDialog:ProgressDialog
    val EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH"
    val VIDEO_TOTAL_DURATION = "VIDEO_TOTAL_DURATION"
    val absolutePath="absolutePath"
    private var permission = 0
    var position: Double? = null
    private var fileName:String=""



    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        {
            permission = if (it) 1
            else 0
        }

    var onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if(intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE){
                intent.extras?.let {

                    //retrieving the file
                    val downloadedFileId = it.getLong(DownloadManager.EXTRA_DOWNLOAD_ID)
                    val downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val uri: Uri = downloadManager.getUriForDownloadedFile(downloadedFileId)

                   val  file  :File =  File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), fileName+".mov");
                    if(file.exists()){
                        val intent = Intent(requireActivity(), VideoTrim::class.java)
                        intent.putExtra(EXTRA_VIDEO_PATH,  uri.toString())
                        intent.putExtra(absolutePath,  file.absolutePath)
                        if(getMediaDuration(uri)!=null)
                           intent.putExtra(VIDEO_TOTAL_DURATION, getMediaDuration(uri))
                             progressDialog.dismiss();
                        startActivity(intent)
                       // Toast.makeText(requireContext(), file.absolutePath, Toast.LENGTH_LONG).show()
                    }



                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        playerView = root.findViewById(R.id.PlayerView)
        likeButton = root.findViewById(R.id.switch_like)
        shareButton = root.findViewById(R.id.share)
        retryLayout = root.findViewById(R.id.layout_retry)
        retryTextView = root.findViewById(R.id.text_retry)
        loading = root.findViewById(R.id.loading)
        requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        position = savedInstanceState?.getDouble("position")

         progressDialog = ProgressDialog(requireActivity())


        return root
    }


    override fun onStart() {
        super.onStart()
        initPlayer()
        downloadManger = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        requireActivity().registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.player.playWhenReady = false
      requireContext().unregisterReceiver(onComplete)
    }

    override fun onPause() {
        super.onPause()
        position = mediaPlayer.player.currentPosition.toDouble()
    }


    private fun play() {
        homeViewModel.urls.observe(viewLifecycleOwner) { urls ->
            if (urls.isEmpty()) {
                setViews(false)
            } else {
                setViews(true)
                listOfUrl = urls

                mediaPlayer.play(homeViewModel.getPlayerState(), urls)

            }
        }

    }


    private fun initPlayer() {
        mediaPlayer = mediaPlayerImplementation.getInstance(requireContext())
        playerView.player = mediaPlayer.player
        mediaPlayer.player.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {
                Log.d(TAG, "Player Error:${error.message!!}")
                homeViewModel.setPlayerState(mediaPlayer.player)
                setViews(false)
                super.onPlayerError(error)
            }
        })

        play()
    }

    private fun releasePlayer() {
        homeViewModel.setPlayerState(playerView.player ?: return)
        mediaPlayer.releasePlayer()
        playerView.player = null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private fun setViews(isEnabled: Boolean) {
        loading.visibility = View.GONE
        if (isEnabled) {
            shareButton.visibility = View.VISIBLE
            playerView.visibility = View.VISIBLE
            shareButton.setOnClickListener { share() }

            val onSwipeListener: OnSwipeListener = object : OnSwipeListener() {
                override fun onSwipe(direction: Direction?): Boolean {
                    return when (direction) {
                        Direction.up -> {
                            if (mediaPlayer.player.hasNext()) {
                                mediaPlayer.player.playWhenReady=false
                                mediaPlayer.player.next()

                            }
                            true
                        }

                        Direction.down -> {
                            if (mediaPlayer.player.hasPrevious()) {
                                mediaPlayer.player.previous()

                            }
                            true
                        }
                        else -> super.onSwipe(direction)
                    }
                }
            }
            gestureDetector = GestureDetector(requireContext(), onSwipeListener)
            playerView.setOnTouchListener(this)
        } else {

            playerView.visibility = View.GONE
            shareButton.visibility = View.GONE

        }
        setRetryLayout(!isEnabled)
    }

    private fun setRetryLayout(isEnabled: Boolean) {
        if (isEnabled) {
            retryLayout.visibility = View.VISIBLE
            retryTextView.visibility = View.VISIBLE
            retryLayout.setOnClickListener {
                homeViewModel.retry.value = !(homeViewModel.retry.value!!)
                mediaPlayer.player.retry()
                loading.visibility = View.VISIBLE
            }
        } else {
            retryLayout.visibility = View.GONE
            retryTextView.visibility = View.GONE
        }
    }

    private fun share() {
        homeViewModel.DownloadUrls.observe(viewLifecycleOwner) { DownloadUrls ->
            if (DownloadUrls.isNotEmpty()) {
                listOfDownloadUrl = DownloadUrls
                val DownloadUrl = listOfDownloadUrl[mediaPlayer.player.currentWindowIndex + 1]
                val sdf = SimpleDateFormat("hh:mm:ss")
                val currentDate = sdf.format(Date())

                if (permission == 1) {
                    fileName=currentDate

                    download(DownloadUrl, fileName)


                } else {
                    Toast.makeText(requireContext(), "Permission Denied...!", Toast.LENGTH_LONG).show()
                }


            }

        }
    }

    private fun download(url: String, fileName: String) {
        try {

            var VideoLink = Uri.parse(url)
            var request = DownloadManager.Request(VideoLink)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                .setMimeType("video/mov")
                .setAllowedOverRoaming(false)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(fileName)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_MOVIES,
                     fileName + ".mov"
                )
            downloadManger.enqueue(request)

            progressDialog.setTitle("Video Share ")
            progressDialog.setMessage("Application is loading, please wait")
            progressDialog.show()
            Toast.makeText(requireContext(), "<<<<video begin downloading>>>>", Toast.LENGTH_LONG).show()



        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_LONG).show()
        }

    }

   private fun getMediaDuration(uriOfFile: Uri): Int {
       if (uriOfFile != null) {
           val mp: MediaPlayer = MediaPlayer.create(requireActivity(), uriOfFile)
           return mp.getDuration()
       } else
           return 0;
   }

}




