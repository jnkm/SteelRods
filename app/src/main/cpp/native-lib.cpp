
#include <jni.h>
#include <string>
#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/features2d.hpp>

extern "C" {

void convertToGrayScale(jlong addrImage)
{
    cv::Mat* inputImage_p = (cv::Mat*)addrImage;
    cv::cvtColor(*inputImage_p, *inputImage_p, CV_BGR2GRAY);
}

void
Java_com_example_jordan_steelrods_ImagePreview_getBlobKeypoints(JNIEnv *env, jobject,
                                                                jlong addrImage) {
    // Firstly convert the input image to grey.
    convertToGrayScale(addrImage);

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

    // Set up detector with params.
    cv::Ptr<cv::SimpleBlobDetector> detector = cv::SimpleBlobDetector::create(params);

    std::vector<cv::KeyPoint> keypoints;

    cv::Mat* inputImage_p = (cv::Mat*)addrImage;

    detector->detect(*inputImage_p, keypoints);

    // This will override the given image. We can't return Mats or images from native methods to
    // the java code. Instead we must overrise the memory address passed in as the argument.
    cv::drawKeypoints(*inputImage_p, keypoints, *inputImage_p,
                      cv::Scalar(0, 0, 255), cv::DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
}

}

