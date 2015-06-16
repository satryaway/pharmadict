package com.samstudio.pharmadict.helpers;

import android.content.Context;


/**
 * Activites that wish to be notified about results
 * in onPostExecute of an AsyncTask must implement
 * this interface.
 *
 * This is the basic Observer pattern.
 */
public interface ResultsListener {
    public void onResultsSucceeded(String result, Context context);
    public void onResultsFailed(Context context);
    }
