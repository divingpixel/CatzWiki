package com.epikron.catzwiki.ui.fragments

import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.FragmentManager
import com.epikron.catzwiki.R
import com.epikron.catzwiki.ui.fragments.BottomFragment.FragmentType.CAT_DETAILS
import com.epikron.catzwiki.utils.exhaustive
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


object BottomFragment {

	enum class FragmentType(val fullScreen: Boolean? = true) {
		CAT_DETAILS
	}

	private val fragmentsList: ArrayList<FragmentType> = arrayListOf()
	private var timeStamp: Long = 0L

	fun popUp(
		type: FragmentType,
		fragmentManager: FragmentManager,
		bundle: Bundle = Bundle(),
		hasCloseButton: Boolean = false,
		action: ((fragmentData: Bundle?) -> Unit)? = null
	) {
		val lastType = fragmentsList.lastOrNull()
		val timeDifference = System.currentTimeMillis() - timeStamp
		if (type != lastType || type == lastType && timeDifference > 1000) {
			timeStamp = System.currentTimeMillis()
			val dialog = BottomSheetFragment.newInstance(
				fragmentType = type,
				hasCloseButton = hasCloseButton,
				args = bundle
			)
			dialog.show(fragmentManager, type.name)
			dialog.onDismissListener = { fragmentData ->
				action?.invoke(fragmentData)
				fragmentsList.removeLastOrNull()
			}
			fragmentsList.add(type)
		}
	}
}

class BottomSheetFragment(
	private val fragmentType: BottomFragment.FragmentType,
	private val hasCloseButton: Boolean
) : BottomSheetDialogFragment() {

	companion object {
		private const val DEFAULT_HEIGHT_PERCENT = 95
		private const val DIM_DIVIDER = 2f
		private const val DIM_OPACITY = 0.5f

		fun newInstance(
			fragmentType: BottomFragment.FragmentType,
			hasCloseButton: Boolean = true,
			args: Bundle = Bundle(),
		) = BottomSheetFragment(fragmentType, hasCloseButton).apply { arguments = args }
	}

	var onDismissListener: ((data: Bundle?) -> Unit)? = null

	private var fragmentData: Bundle? = null
	private val heightInPixels = Resources.getSystem().displayMetrics.heightPixels

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? = View.inflate(context, R.layout.bottom_sheet_dialog, null)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
		dialog?.setOnShowListener { setupBottomSheet(it) }
		bindFragment(fragmentType)
	}

	override fun onDismiss(dialog: DialogInterface) {
		super.onDismiss(dialog)
		onDismissListener?.invoke(fragmentData)
	}

	private fun bindFragment(fragmentType: BottomFragment.FragmentType) {
		val fragment = when (fragmentType) {
			CAT_DETAILS -> CatDetailFragment.newInstance(arguments)
		}.exhaustive
		childFragmentManager.beginTransaction()
			.replace(R.id.bottom_sheet_container, fragment, fragmentType.name)
			.show(fragment)
			.commit()
		fragment.onCloseListener = { bundle ->
			fragmentData = bundle
			dismiss()
		}
	}

	private fun addCloseButton(parentView: FrameLayout) {
		val imageViewLayoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
		imageViewLayoutParams.setMargins(
			0,
			resources.getDimensionPixelSize(R.dimen.standard_margin),
			resources.getDimensionPixelSize(R.dimen.standard_margin),
			0
		)
		imageViewLayoutParams.gravity = Gravity.END or Gravity.TOP

		val closeButtonView = ImageView(context)
		closeButtonView.id = View.generateViewId()
		closeButtonView.setImageDrawable(
			ContextCompat.getDrawable(
				requireContext(),
				R.drawable.ic_cancel
			)
		)
		closeButtonView.layoutParams = imageViewLayoutParams
		closeButtonView.setOnClickListener { dismiss() }
		parentView.addView(closeButtonView)
	}

	private fun setupBottomSheet(dialogInterface: DialogInterface) {
		val bottomSheetDialog = dialogInterface as BottomSheetDialog
		bottomSheetDialog.dismissWithAnimation = true
		bottomSheetDialog.show()

		val bottomSheet = bottomSheetDialog
			.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
		bottomSheet.setBackgroundColor(Color.TRANSPARENT)
		if (hasCloseButton) addCloseButton(bottomSheet)

		val height = if (fragmentType.fullScreen == true) {
			heightInPixels * DEFAULT_HEIGHT_PERCENT / 100
		} else {
			bottomSheet.children.first().measuredHeight
		}
		this.view?.layoutParams?.height = height

		val sheetBehavior: BottomSheetBehavior<View> = BottomSheetBehavior.from(bottomSheet)
		sheetBehavior.run {
			isDraggable = !hasCloseButton
			state = BottomSheetBehavior.STATE_EXPANDED
			peekHeight = height
			setCustomBehaviour()
		}
	}

	private fun BottomSheetBehavior<View>.setCustomBehaviour(onStateChangedBehaviour: (newState: Int) -> Unit = {}) {
		addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
			override fun onSlide(bottomSheet: View, slideOffset: Float) {
				val dimValue = (slideOffset.plus(1) / DIM_DIVIDER).coerceIn(0f, DIM_OPACITY)
				dialog?.window?.setDimAmount(dimValue)
			}

			override fun onStateChanged(bottomSheet: View, newState: Int) {
				onStateChangedBehaviour(newState)
				if (newState == STATE_COLLAPSED) onDismissListener?.invoke(fragmentData)
			}
		})
	}
}
