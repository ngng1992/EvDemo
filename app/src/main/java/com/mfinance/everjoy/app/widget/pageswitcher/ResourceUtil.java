package com.mfinance.everjoy.app.widget.pageswitcher;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;


public class ResourceUtil
{
    private Context mContext;
    private AttributeSet mAttrSet;
    private TypedArray mCustomAttributes;
    
    
    public ResourceUtil (Context pContext, AttributeSet pAttrSet, int[] pAttr)
    {
        super();
        
        mContext = pContext;
        mAttrSet = pAttrSet;
        if (mAttrSet != null) {
            mCustomAttributes = pContext.obtainStyledAttributes(mAttrSet, pAttr);
        }
    }

    /**
     * Retrieves a resource Id stored in a custom attribute (see attr.xml files) if one is defined.
     * In case it is not, returns the default resource Id specified.
     * 
     * @param pCustomAttributeId Id of the custom attribute containing the searched resource.
     * @param pDefaultId Default Id to return (not a custom attribute Id but a resource Id)
     * @return Resource Id defined in the specified custom attribute or pDefaultId if it is not.
     */
    public int getResourceId (int pCustomAttributeId, int pDefaultId)
    {
        if (mCustomAttributes != null) {
            return mCustomAttributes.getResourceId(pCustomAttributeId, pDefaultId);
        } else {
            return pDefaultId;
        }
    }

    /**
     * Retrieves a string stored in a custom attribute (see attr.xml files) if one is defined. In
     * case it is not, returns the default string specified.
     * 
     * @param pCustomAttributeId Id of the custom attribute containing the searched string.
     * @param pDefaultId Default Id to return (not a custom attribute Id but a resource Id)
     * @return String defined in the specified custom attribute or pDefaultId if it is not.
     */
    public String getString (int pCustomAttributeId, String pDefaultString)
    {
        if (mCustomAttributes != null) {
            String lResult = mCustomAttributes.getString(pCustomAttributeId);
            if (lResult != null) {
                return lResult;
            } else {
                return pDefaultString;
            }
        } else {
            return pDefaultString;
        }
    }

    /**
     * Retrieves a string stored in a custom attribute (see attr.xml files) if one is defined. In
     * case it is not, returns the default string specified.
     * 
     * @param pCustomAttributeId Id of the custom attribute containing the searched string.
     * @param pDefaultId Default Id to return (not a custom attribute Id but a resource Id)
     * @return String defined in the specified custom attribute or pDefaultId if it is not.
     */
    public String getString (int pCustomAttributeId, int pDefaultStringId)
    {
        if (mCustomAttributes != null) {
            String lResult = mCustomAttributes.getString(pCustomAttributeId);
            if (lResult != null) {
                return lResult;
            } else {
                return mContext.getString(pDefaultStringId);
            }
        } else {
            return mContext.getString(pDefaultStringId);
        }
    }
}
