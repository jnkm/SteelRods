#include <jni.h>
#include <string>
#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

extern "C" {


JNIEXPORT jstring

JNICALL
Java_com_example_jordan_steelrods_ImagePreview_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

jstring
Java_com_example_jordan_steelrods_ImagePreview_imageTest(JNIEnv *env, jobject, jlong addrImage)
{
    cv::Mat* inputImage_p = (cv::Mat*)addrImage;
    cv::cvtColor(*inputImage_p, *inputImage_p, CV_BGR2GRAY);
    std::string hello2 = "Hello from imageTest";
    return env->NewStringUTF(hello2.c_str());
}

jstring
Java_com_example_jordan_steelrods_ImagePreview_getBlobKeypoints(JNIEnv *env, jobject, jlong addrImage)
{
// Setup SimpleBlobDetector parameters.
    cv::SimpleBlobDetector::Params params;

    // Change thresholds
    params.minThreshold = 50;
    params.maxThreshold = 255;

    // Filter by Area.
    params.filterByArea = true;
    params.minArea = 75;

    // Filter by Circularity
    params.filterByCircularity = true;
    params.minCircularity = 0.01;

    // Filter by Convexity
    params.filterByConvexity = true;
    params.minConvexity = 0.01;

    // Filter by Inertia
    params.filterByInertia = true;
    params.minInertiaRatio = 0.01;

    params.blobColor = 255;

#if CV_MAJOR_VERSION < 3   // If you are using OpenCV 2

    // Set up detector with params
	SimpleBlobDetector detector(params);

	// You can use the detector this way
	// detector.detect( im, keypoints);

#else

    // Set up detector with params
    cv::Ptr<SimpleBlobDetector> detector = SimpleBlobDetector::create(params);

    // SimpleBlobDetector::create creates a smart pointer.
    // So you need to use arrow ( ->) instead of dot ( . )
    // detector->detect( im, keypoints);

#endif

    vector<KeyPoint> keypoints;

    detector->detect(img, keypoints);

    return keypoints;
}

jstring
Java_com_example_jordan_steelrods_ImagePreview_addKeypointsToImage(JNIEnv *env, jobject, jlong addrImage)
{
    cv::Mat img_with_keypoints;
    drawKeypoints(image, keypoints, img_with_keypoints, Scalar(0,0,255), DrawMatchesFlags::DRAW_RICH_KEYPOINTS);

    return img_with_keypoints;
}

}