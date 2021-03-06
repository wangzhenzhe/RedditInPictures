package com.antew.redditinpictures.library.image;

import com.antew.redditinpictures.library.apikeys.FlickrApiKey;
import com.antew.redditinpictures.library.enums.ImageSize;
import com.antew.redditinpictures.library.enums.ImageType;
import com.antew.redditinpictures.library.gson.BooleanDeserializer;
import com.antew.redditinpictures.library.imageapi.Flickr;
import com.antew.redditinpictures.library.imageapi.Flickr.FlickrImage;
import com.antew.redditinpictures.library.imageapi.Flickr.FlickrSize;
import com.antew.redditinpictures.library.logging.Log;
import com.antew.redditinpictures.library.network.SynchronousNetworkApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class FlickrImageType extends Image {
    public static final String  TAG        = FlickrImageType.class.getSimpleName();
    private static final String FLICKR_URL = "http://api.flickr.com/services/rest/?method=flickr.photos.getSizes&api_key=" + FlickrApiKey.KEY
                                                   + "&photo_id=%s&format=json&nojsoncallback=1";
    private static final String URL_REGEX = "^http://(?:\\w+).?flickr.com/(?:.*)/([\\d]{10})/?(?:.*)?$";
    private Flickr              mFlickr    = null;

    public FlickrImageType(String url) {
        super(url);
    }

    /**
     * Resolve the flickr image for the input URL
     * 
     * @param url
     *            The URL to resolve an image from
     * @return An {@link Flickr} instance representing the image
     */
    private Flickr resolve() {
        mFlickr = resolveFlickrImageFromHash(getHash());

        return mFlickr;
    }

    /**
     * Resolve an image from Flickr
     * 
     * @param url
     *            The URL to resolve
     * @return A {@link Flickr} instance
     */
    public static Flickr resolveFlickrImageFromHash(String hash) {
        Flickr flickr = null;

        if (hash != null) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Boolean.class, new BooleanDeserializer()).create();
            try {
                String json = SynchronousNetworkApi.downloadUrl(String.format(FLICKR_URL, hash));
                flickr = gson.fromJson(json, Flickr.class);
            } catch (JsonSyntaxException e) {
                Log.e(TAG, "Error parsing JSON in resolveFlickrImage", e);
            }

        }

        return flickr;
    }

    @Override
    public String getSize(ImageSize size) {
        assert size != null : "ImageSize must not be null";

        if (mFlickr == null)
            mFlickr = resolve();
        
        if (mFlickr == null) {
            Log.i(TAG, "Received null Flickr object");
            return null;
        }

        FlickrSize flickrSize = FlickrSize.ORIGINAL;
        if (ImageSize.SMALL_SQUARE.equals(size))
            flickrSize = FlickrSize.THUMBNAIL;

        FlickrImage fi = mFlickr.getSize(flickrSize);
        if (fi != null)
            return fi.getSource();

        return null;
    }

    @Override
    public ImageType getImageType() {
        return ImageType.FLICKR_IMAGE;
    }

    @Override
    public String getRegexForUrlMatching() {
        return URL_REGEX;
    }

}
