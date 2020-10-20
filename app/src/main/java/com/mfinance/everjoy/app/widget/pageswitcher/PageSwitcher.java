package com.mfinance.everjoy.app.widget.pageswitcher;


import java.util.ArrayList;



import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ViewFlipper;

import com.mfinance.everjoy.R;


public class PageSwitcher extends FrameLayout
{
    private static final double PREDICTION_THRESHOLD = 1.0;
    
    // UI elements.
    private GestureOverlayView mUIGestureOverlay;
    private ViewFlipper        mUIViewFlipper;
    private GestureLibrary     mGestureLibrary;

    // Names of the gesture that will cause a switch to the previous or next view. 
    private String mPreviousGestureName; 
    private String mNextGestureName;

    // View switch transitions.
    private Animation mPreviousIn;
    private Animation mPreviousOut;
    private Animation mNextIn;
    private Animation mNextOut;



    public PageSwitcher (Context pContext, AttributeSet pAttrsSet)
    {
        super(pContext, pAttrsSet);
        initialize(pContext, pAttrsSet);
    }
    
    public PageSwitcher (Context pContext)
    {
        super(pContext);
        initialize(pContext, null);
    }

    private void initialize(Context pContext, AttributeSet pAttrsSet)
    {
        ResourceUtil lResourceUtil = new ResourceUtil(pContext, pAttrsSet, R.styleable.PageSwitcher);

        // Initializes the gesture library.
        int lGestureId = lResourceUtil.getResourceId(R.styleable.PageSwitcher_gestureLibrary, R.raw.pageswitcher_gesture);
        mGestureLibrary = GestureLibraries.fromRawResource(getContext(), lGestureId);
        if (!mGestureLibrary.load()) {
            throw new RuntimeException("Could not load gesture library");
        }
        
        // Retrieves gesture names and animation.
        mPreviousGestureName = lResourceUtil.getString(R.styleable.PageSwitcher_previousGesture, R.string.pageswitcher_previous_gesture);
        mNextGestureName     = lResourceUtil.getString(R.styleable.PageSwitcher_nextGesture, R.string.pageswitcher_next_gesture);

        mPreviousIn  = AnimationUtils.loadAnimation(pContext, lResourceUtil.getResourceId(R.styleable.PageSwitcher_previousIn,  R.anim.pageswitcher_previous_in));
        mPreviousOut = AnimationUtils.loadAnimation(pContext, lResourceUtil.getResourceId(R.styleable.PageSwitcher_previousOut, R.anim.pageswitcher_previous_out));
        mNextIn      = AnimationUtils.loadAnimation(pContext, lResourceUtil.getResourceId(R.styleable.PageSwitcher_nextIn,      R.anim.pageswitcher_next_in));
        mNextOut     = AnimationUtils.loadAnimation(pContext, lResourceUtil.getResourceId(R.styleable.PageSwitcher_nextOut,     R.anim.pageswitcher_next_out));

        // Initializes UI components.
        mUIViewFlipper = new ViewFlipper(pContext, pAttrsSet);
        mUIViewFlipper.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        mUIGestureOverlay = new GestureOverlayView(pContext, pAttrsSet);
        mUIGestureOverlay.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        mUIGestureOverlay.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_SINGLE);
        mUIGestureOverlay.setEventsInterceptionEnabled(true);
        mUIGestureOverlay.setGestureVisible(false);
        mUIGestureOverlay.addOnGesturePerformedListener(new GestureListener());
        
        mUIGestureOverlay.addView(mUIViewFlipper, -1, mUIViewFlipper.getLayoutParams());
        super.addView(mUIGestureOverlay, -1, mUIGestureOverlay.getLayoutParams());
    }



    /**
     * Listens for gestures and acts according to them (i.e. switch to next or previous screen).
     */
    private class GestureListener implements GestureOverlayView.OnGesturePerformedListener
    {
        @Override
        public void onGesturePerformed (GestureOverlayView pOverlay, Gesture pGesture)
        {
            ArrayList<Prediction> lPredictions = mGestureLibrary.recognize(pGesture);
            int lPredictionSize = lPredictions.size();
    
            // If a gesture is found with a good match...
            for (int i = 0; i < lPredictionSize; i++) {
                Prediction lPrediction = lPredictions.get(i);
                if (lPrediction.score > PREDICTION_THRESHOLD) {
                    // ...then executes the corresponding action, i.e. shows previous or next view. Scrolling is
                    // performed through the Android animation mechanism implemented in ViewFlipper component.
                    if (lPrediction.name.equals(mPreviousGestureName)) {
                        mUIViewFlipper.setInAnimation(mPreviousIn);
                        mUIViewFlipper.setOutAnimation(mPreviousOut);
                        mUIViewFlipper.showPrevious();
                    } else if (lPrediction.name.equals(mNextGestureName)) {
                        mUIViewFlipper.setInAnimation(mNextIn);
                        mUIViewFlipper.setOutAnimation(mNextOut);
                        mUIViewFlipper.showNext();
                    }
                    break;
                }
            }
        }
    }



    @Override
    public void addView (View pChild, int pWidth, int pHeight)
    {
        mUIViewFlipper.addView(pChild, pWidth, pHeight);
    }

    @Override
    public void addView (View pChild, int pIndex, android.view.ViewGroup.LayoutParams pParams)
    {
        mUIViewFlipper.addView(pChild, pIndex, pParams);
    }

    @Override
    public void addView (View pChild, int pIndex)
    {
        mUIViewFlipper.addView(pChild, pIndex);
    }

    @Override
    public void addView (View pChild, android.view.ViewGroup.LayoutParams pParams)
    {
        mUIViewFlipper.addView(pChild, pParams);
    }

    @Override
    public void addView (View pChild)
    {
        mUIViewFlipper.addView(pChild);
    }

    @Override
    public void removeAllViews ()
    {
        mUIViewFlipper.removeAllViews();
    }

    @Override
    public void removeAllViewsInLayout ()
    {
        mUIViewFlipper.removeAllViewsInLayout();
    }

    @Override
    public void removeView (View pView)
    {
        mUIViewFlipper.removeView(pView);
    }

    @Override
    public void removeViewAt (int pIndex)
    {
        mUIViewFlipper.removeViewAt(pIndex);
    }

    @Override
    public void removeViewInLayout (View pView)
    {
        mUIViewFlipper.removeViewInLayout(pView);
    }

    @Override
    public void removeViews (int pStart, int pCount)
    {
        mUIViewFlipper.removeViews(pStart, pCount);
    }

    @Override
    public void removeViewsInLayout (int pStart, int pCount)
    {
        mUIViewFlipper.removeViewsInLayout(pStart, pCount);
    }



    public void setGestureLibrary (GestureLibrary pGestureLibrary)
    {
        mGestureLibrary = pGestureLibrary;
    }

    public void setPreviousGestureName (String pPreviousGestureName)
    {
        mPreviousGestureName = pPreviousGestureName;
    }

    public void setNextGestureName (String pNextGestureName)
    {
        mNextGestureName = pNextGestureName;
    }

    public void setPreviousIn (Animation pPreviousIn)
    {
        mPreviousIn = pPreviousIn;
    }

    public void setPreviousOut (Animation pPreviousOut)
    {
        mPreviousOut = pPreviousOut;
    }

    public void setNextIn (Animation pNextIn)
    {
        mNextIn = pNextIn;
    }

    public void setNextOut (Animation pNextOut)
    {
        mNextOut = pNextOut;
    }
}
