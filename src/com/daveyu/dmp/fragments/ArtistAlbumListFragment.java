package com.daveyu.dmp.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ArtistAlbumListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>  {

	private static final int LOADER_ID = 0;
	private SimpleCursorAdapter adapter;
	PassLabel label_passer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLoaderManager().initLoader(LOADER_ID, null, this);
		
		String[] mProjection = {
				MediaStore.Audio.Albums.ALBUM,
				MediaStore.Audio.Albums.FIRST_YEAR,
				MediaStore.Audio.Albums.ALBUM_ART
			};
		
		int[] mTo = {
				com.daveyu.dmp.R.id.text_1,
				com.daveyu.dmp.R.id.text_2,
				com.daveyu.dmp.R.id.album_thumbnail
			};
		
		adapter = new SimpleCursorAdapter(
				getActivity().getApplicationContext(),
				com.daveyu.dmp.R.layout.list_item_albums,
				null,
				mProjection,
				mTo,
				0
			);
		
		setListAdapter(adapter);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        try {
            label_passer = (PassLabel) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(com.daveyu.dmp.R.layout.listview_layout, container, false);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
		String mProjection[] = {
				MediaStore.Audio.Albums._ID,
				MediaStore.Audio.Albums.ALBUM,
				MediaStore.Audio.Albums.ARTIST,
				MediaStore.Audio.Albums.FIRST_YEAR,
				MediaStore.Audio.Albums.ALBUM_ART
			};
		
		String ARTIST_NAME = label_passer.getArtistName();
		String[] selectionArgs = {""};
		selectionArgs[0] = ARTIST_NAME;
		String selectionClause = "ARTIST = ?";
		String sortOrder = "MINYEAR";
		
		CursorLoader cursorLoader = new CursorLoader(
				getActivity(),
				MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
				mProjection,
				selectionClause,
				selectionArgs,
				sortOrder
			);
		
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.changeCursor(null);
	}
	
	/**
	 * Callback method telling Activity to pass artist name to Fragment
	 */
	public interface PassLabel {
		public String getArtistName();
	}
	
}
