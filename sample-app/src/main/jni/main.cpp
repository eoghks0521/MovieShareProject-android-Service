#include<jni.h>
#include "life_knowledge4_videotrimmersample_Brother.h"

#include <opencv2/opencv.hpp>
#include <iostream>
#include <stdio.h>
#include <string>


using namespace cv;
using namespace std;

extern "C" JNIEXPORT jstring

JNICALL
Java_life_knowledge4_videotrimmersample_Brother_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
        std::string hello = "Hello from C++";
        return env->NewStringUTF(hello.c_str());
}

double angle(cv::Point pt1, cv::Point pt2, cv::Point pt0) {
        double dx1 = pt1.x - pt0.x;
        double dy1 = pt1.y - pt0.y;
        double dx2 = pt2.x - pt0.x;
        double dy2 = pt2.y - pt0.y;
        return (dx1*dx2 + dy1*dy2) / sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
}

void overlayImage(Mat* src, Mat* overlay, const Point& location)
{
        for (int y = max(location.y, 0); y < src->rows; ++y)
        {
                int fY = y - location.y;

                if (fY >= overlay->rows)
                        break;

                for (int x = max(location.x, 0); x < src->cols; ++x)
                {
                        int fX = x - location.x;

                        if (fX >= overlay->cols)
                                break;

                        double opacity = ((double)overlay->data[fY * overlay->step + fX * overlay->channels() + 3]) / 255;

                        for (int c = 0; opacity > 0 && c < src->channels(); ++c)
                        {
                                unsigned char overlayPx = overlay->data[fY * overlay->step + fX * overlay->channels() + c];
                                unsigned char srcPx = src->data[y * src->step + x * src->channels() + c];
                                src->data[y * src->step + src->channels() * x + c] = srcPx * (1. - opacity) + overlayPx * opacity;
                        }
                }
        }
}

cv::Mat debugSquares(std::vector<std::vector<cv::Point> > squares, cv::Mat image)
{
        for (int i = 0; i< squares.size(); i++) {
                // draw contour
                cv::drawContours(image, squares, i, cv::Scalar(255, 0, 0), 2, 8, std::vector<cv::Vec4i>(), 0, cv::Point());

                // draw bounding rect
                cv::Rect rect = boundingRect(cv::Mat(squares[i]));
                //cv::rectangle(image, rect.tl(), rect.br(), cv::Scalar(0, 255, 0), 2, 8, 0);

                // draw rotated rect
                /*
                cv::RotatedRect minRect = minAreaRect(cv::Mat(squares[i]));
                cv::Point2f rect_points[4];
                minRect.points(rect_points);
                for (int j = 0; j < 4; j++) {
                    cv::line(image, rect_points[j], rect_points[(j + 1) % 4], cv::Scalar(0, 255, 255), 1, 1); // blue
                }
                */
        }

        return image;
}

int thumbnail = 0;
float object_area;
float obj_center_x;
float obj_center_y;
float obj_range;
float object_width;
float object_height;

float LeftTop_x;
float LeftTop_y;

float LeftBottom_x;
float LeftBottom_y;

float RightBottom_x;
float RightBottom_y;

float RightTop_x;
float RightTop_y;


extern "C"
JNIEXPORT void JNICALL
Java_life_knowledge4_videotrimmersample_Brother_ConvertRGBtoGray( JNIEnv *env,
                                                                  jobject instance,
                                                                  jlong matAddrInput,
                                                                  jlong matAddrResult, jlong video_mat, jlong thumbnail_mat, jint reset, jint thumb
){
        //jlong video_mat

        // 입력 RGBA 이미지를 GRAY 이미지로 변환

        if (reset == 1){
                thumbnail = 0;
        }

        Mat &thumbnail_result = *(Mat *)thumbnail_mat;

        Mat &frame = *(Mat *)matAddrInput;

        Mat &matResult = *(Mat *)matAddrResult;


        //Mat frame2;
        Mat &frame2 = *(Mat *)video_mat;

        RNG rng(12345);
        Mat warp_mat(2, 3, CV_32FC1);
        Mat warp_dst;

        Mat warp_mat_c(2, 4, CV_32FC1);
        Mat warp_dst_c;

        //int thumbnail = 0;

        int radius;
        float circle_x;
        float circle_y;
        //--- INITIALIZE VIDEOCAPTURE
        // open the default camera using default API
        //cap.open(0);
        //cap2.open("dog2.mp4");
        // OR advance usage: select any API backend

        // check if we succeeded

        //--- GRAB AND WRITE LOOP

        // wait for a new frame from camera and store it into 'frame'
        //cap2.read(frame2);

        // check if we succeeded

        //int w = cap.get(CV_CAP_PROP_FRAME_WIDTH);
        //int h = cap.get(CV_CAP_PROP_FRAME_HEIGHT);
        int w = frame.cols;
        int h = frame.rows;

        int length = 20;
        int p = 70;


        if (thumbnail == 0) {
                //Left Top
                //line(frame, Point(p, p), Point(length + p, p), (0, 0, 255), 5);
                //line(frame, Point(p, p), Point(p, length + p), (0, 0, 255), 5);
                circle(frame, Point(p, p), 5, Scalar(255, 0, 0), -1);

                //Right Top
                //line(frame, Point(w - length - p, p), Point(w - p, p), (0, 0, 255), 5);
                //line(frame, Point(w - p, p), Point(w - p, length + p), (0, 0, 255), 5);
                circle(frame, Point(w - p, p), 5, Scalar(255, 0, 0), -1);

                //Left Bottom
                //line(frame, Point(p, h - length - p), Point(p, h - p), (0, 0, 255), 5);
                //line(frame, Point(p, h - p), Point(length + p, h - p), (0, 0, 255), 5);
                circle(frame, Point(p, h - p), 5, Scalar(255, 0, 0), -1);

                //Right Bottom
                //line(frame, Point(w - length - p, h - p), Point(w - p, h - p), (0, 0, 255), 5);
                //line(frame, Point(w - p, h - length - p), Point(w - p, h - p), (0, 0, 255), 5);
                circle(frame, Point(w - p, h - p), 5, Scalar(255, 0, 0), -1);
        }
        /*
        left_top_p = [p,p]
        right_top_p = [w-p,p]
        left_bottom_p = [p,h-p]
        right_botton_p = [w-p,h-p]
        */

        int square_width = (w - length - p) - p;
        int square_height = (h - p) - p;

        int square_area = square_width * square_height;

        if (thumbnail == 0) {
                object_area = (float)square_area;
                object_width = square_width;
                object_height = square_height;
        }

        if (thumbnail == 0) {
                /*
                radius = square_width*0.7;
                circle_x = w / 2;
                circle_y = h / 2;
                */
                obj_center_x = w / 2;
                obj_center_y = h / 2;
        }


        /*
        Mat mask(frame.size(), frame.type(), Scalar::all(0));
        circle(mask, Point(circle_x, circle_y), radius, Scalar::all(255), -1);
        Mat cropped = frame & mask;
        */


        std::vector<std::vector<cv::Point> > squares;
        std::vector<std::vector<cv::Point> > squares_tmp;
        cv::Mat pyr, timg, gray0(frame.size(), CV_8U), gray;
        int thresh = 100, N = 11;
        cv::pyrDown(frame, pyr, cv::Size(frame.cols / 2, frame.rows / 2));
        cv::pyrUp(pyr, timg, frame.size());
        std::vector<std::vector<cv::Point> > contours;
        for (int c = 0; c < 3; c++) {
                int ch[] = { c, 0 };
                mixChannels(&timg, 1, &gray0, 1, ch, 1);
                for (int l = 0; l < N; l++) {
                        if (l == 0) {
                                GaussianBlur(gray0, gray0, Size(3, 3), 0);
                                cv::Canny(gray0, gray, 0, thresh);
                                cv::dilate(gray, gray, cv::Mat(), cv::Point(-1, -1));
                        }
                        else {
                                gray = gray0 >= (l + 1) * 255 / N;
                        }
                        cv::findContours(gray, contours, CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
                        std::vector<cv::Point> approx;
                        for (size_t i = 0; i < contours.size(); i++)
                        {
                                cv::approxPolyDP(cv::Mat(contours[i]), approx, arcLength(cv::Mat(contours[i]), true)*0.02, true);
                                if (approx.size() == 4 &&
                                    (w*h*0.01 < fabs(contourArea(cv::Mat(approx)))) && (fabs(contourArea(cv::Mat(approx))) < (w-30)*(h-30)) &&
                                    (object_area * 0.8 < fabs(contourArea(cv::Mat(approx))) && fabs(contourArea(cv::Mat(approx))) < object_area * 1.2) &&
                                    cv::isContourConvex(cv::Mat(approx))) {
                                        double maxCosine = 0;

                                        for (int j = 2; j < 5; j++)
                                        {
                                                double cosine = fabs(angle(approx[j % 4], approx[j - 2], approx[j - 1]));
                                                maxCosine = MAX(maxCosine, cosine);
                                        }

                                        if (maxCosine < 0.3) {
                                                squares.push_back(approx);
                                                l = N;
                                                c = 3;
                                                break;
                                        }
                                }
                        }
                }
        }

        if(squares.size() != 0){

                squares_tmp = squares;
                for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 3; j++) {
                                float tmp;
                                if (squares[0][j].x > squares[0][j+1].x) {
                                        tmp = squares[0][j+1].x;
                                        squares[0][j+1].x = squares[0][j].x;
                                        squares[0][j].x = tmp;

                                        tmp = squares[0][j+1].y;
                                        squares[0][j + 1].y = squares[0][j].y;
                                        squares[0][j].y = tmp;
                                }
                        }
                }


                LeftTop_x = squares[0][0].x;
                LeftTop_y = squares[0][0].y;

                LeftBottom_x = squares[0][1].x;
                LeftBottom_y = squares[0][1].y;

                RightBottom_x = squares[0][2].x;
                RightBottom_y = squares[0][2].y;

                RightTop_x = squares[0][3].x;
                RightTop_y = squares[0][3].y;



                if (squares[0][0].y > squares[0][1].y) {
                        LeftBottom_x = squares[0][0].x;
                        LeftBottom_y = squares[0][0].y;

                        LeftTop_x = squares[0][1].x;
                        LeftTop_y = squares[0][1].y;
                }


                if (squares[0][2].y > squares[0][3].y) {
                        RightBottom_x = squares[0][3].x;
                        RightBottom_y = squares[0][3].y;

                        RightTop_x = squares[0][2].x;
                        RightTop_y = squares[0][2].y;
                }



                obj_range = 20;

                if((abs(RightTop_x - LeftTop_x) > 0 && abs(RightTop_y - RightBottom_y) > 0) &&
                   (obj_center_x - obj_range <= (abs(RightTop_x + LeftTop_x) / 2 ) && (abs(RightTop_x + LeftTop_x) / 2 ) <= obj_center_x + obj_range) &&
                   (object_width*0.8 <= abs(RightTop_x - LeftTop_x) && abs(RightTop_x - LeftTop_x) <= object_width*1.2) &&
                   (object_height*0.8 <= abs(RightTop_y - RightBottom_y) && abs(RightTop_y - RightBottom_y) <= object_height*1.2)
                        //(obj_center_y - obj_range <= (abs(RightTop_y + RightBottom_y) / 2 ) && (abs(RightTop_y + RightBottom_y) / 2 ) <= obj_center_y + obj_range)
                        ){

                        float w1 = abs(RightBottom_x - LeftBottom_x);
                        float w2 = abs(RightTop_x - LeftTop_x);
                        float h1 = abs(RightTop_y - RightBottom_y);
                        float h2 = abs(LeftTop_y - LeftBottom_y);

                        float maxWidth = max(w1, w2);
                        float maxHeight = max(h1, h2);

                        object_area = maxWidth * maxHeight;

                        if (thumbnail == 0) {
                                /*
                                cout << LeftTop_x << "," << LeftTop_y << endl;
                                cout << LeftBottom_x << "," << LeftBottom_y << endl;
                                cout << RightBottom_x << "," << RightBottom_y << endl;
                                cout << RightTop_x << "," << RightTop_y << endl;
                                cout << "=============" << endl;
                                */

                                /*
                                left_top_p = [p,p]
                                right_top_p = [w-p,p]
                                left_bottom_p = [p,h-p]
                                right_bottom_p = [w-p,h-p]
                                */

                                int range = 50;
                                /*
                                if (((p - range <= LeftTop_x && LeftTop_x <= p + range) && (p - range <= LeftTop_y && LeftTop_y <= p + range)) &&
                                    ((w - p - range <= RightTop_x && RightTop_x <= w - p + range) && (p - range <= RightTop_y && RightTop_y <= p + range)) //&&
                                    //((p - 5 <= LeftBottom_x && LeftBottom_x <= p + 5) && (h - p - 5 <= LeftBottom_y && LeftBottom_y <= h - p + 5)) &&
                                    //((w - p - 5 <= RightBottom_x && RightBottom_x <= w - p + 5) && (h - p - 5 <= RightBottom_y && RightBottom_y <= h - p + 5))
                                    ){

                                    cout << "detect!!" << endl;
                                }
                                */

                                float Mid_x = abs(RightTop_x + LeftTop_x) / 2;
                                float Mid_y = abs(RightTop_y + RightBottom_y) / 2;
                                //cout << Mid_x << "," << Mid_y << endl;
                                //cout << (w) / 2.0 << "," << (h) / 2.0 << endl;

                                if ((Mid_x - range <= (w) / 2.0 && (w) / 2.0 <= Mid_x + range) &&
                                    (Mid_y - range <= (h) / 2.0 && (h) / 2.0 <= Mid_y + range) &&
                                    (abs(RightTop_x - LeftTop_x) - range <= square_width && square_width <= abs(RightTop_x - LeftTop_x) + range)
                                        ) {
                                        debugSquares(squares_tmp, frame);
                                        //cout << "detect!!" << endl;

                                        /*
                                        Rect myROI(p-20, p-20, square_width+60, square_height+60);
                                        Mat croppedRef(frame, myROI);
                                        Mat cropped;
                                        croppedRef.copyTo(cropped);
                                        */

                                        Point2f srcTri[4];
                                        Point2f dstTri[4];

                                        srcTri[0] = Point2f(LeftTop_x, LeftTop_y);
                                        srcTri[2] = Point2f(RightTop_x, RightTop_y);
                                        srcTri[1] = Point2f(RightBottom_x, RightBottom_y);
                                        srcTri[3] = Point2f(LeftBottom_x, LeftBottom_y);

                                        dstTri[0] = Point2f(0, 0);
                                        dstTri[1] = Point2f(maxWidth - 1, 0);
                                        dstTri[2] = Point2f(maxWidth - 1, maxHeight - 1);
                                        dstTri[3] = Point2f(0, maxHeight - 1);

                                        warp_mat_c = getPerspectiveTransform(srcTri, dstTri);

                                        warpPerspective(frame, warp_dst_c, warp_mat_c, Size(maxWidth, maxHeight));


                                        //frame = warp_dst_c;
                                        //imwrite("crop.png", warp_dst_c);


                                        //imshow("test", eye_cropped);
                                        //waitKey(0);

                                        if(thumb == 1) {
                                                thumbnail = 1;
                                                thumbnail_result = warp_dst_c;
                                        }
                                }
                        }

                        else if (thumbnail == 1) {
                                //circle(frame, Point(LeftTop_x, LeftTop_y), 10, Scalar(255, 0, 0), -1);
                                //circle(frame, Point(RightBottom_x, RightBottom_y), 10, Scalar(0, 255, 0), -1);
                                //circle(frame, Point(RightTop_x, RightTop_y), 10, Scalar(0, 0, 255), -1);

                                int v_width = frame2.cols;
                                int v_height = frame2.rows;

                                float x_p = LeftTop_x;
                                float y_p = LeftTop_y;

                                Point2f srcTri[3];
                                Point2f dstTri[3];

                                srcTri[0] = Point2f(0, 0);
                                srcTri[1] = Point2f(v_width, 0);
                                srcTri[2] = Point2f(v_width, v_height);

                                dstTri[0] = Point2f(x_p - 7, y_p - 7);
                                dstTri[1] = Point2f((RightBottom_x - LeftTop_x) + x_p + 7, (RightBottom_y - LeftTop_y) + y_p - 7);
                                dstTri[2] = Point2f((RightTop_x - LeftTop_x) + x_p + 7, (RightTop_y - LeftTop_y) + y_p + 7);

                                warp_mat = getAffineTransform(srcTri, dstTri);

                                warpAffine(frame2, warp_dst, warp_mat, Size(w, h));

                                //frame = warp_dst;


                                Mat src = warp_dst;
                                Mat dst;
                                Mat tmp, thr;

                                cvtColor(src, tmp, CV_BGR2GRAY);
                                threshold(tmp, thr, 100, 255, THRESH_BINARY);

                                Mat rgb[3];
                                split(src, rgb);

                                Mat rgba[4] = { rgb[0],rgb[1],rgb[2],thr };

                                merge(rgba, 4, dst);


                                overlayImage(&frame, &dst, Point(0, 0));

                        }
                        /*
                        radius = maxWidth*0.8;
                        circle_x = (LeftTop_x + RightBottom_x) / 2;
                        circle_y = (LeftTop_y + RightBottom_y) / 2;
                        */

                        obj_center_x = (LeftTop_x + RightBottom_x) / 2;
                        obj_center_y = (LeftTop_y + RightBottom_y) / 2;
                        object_width = abs(RightTop_x - LeftTop_x);
                        object_height = abs(RightTop_y - RightBottom_y);
                }
                else {
                        /*
                        radius = w / 2;
                        circle_x = w / 2;
                        circle_y = h / 2;
                        */
                        /*
                        obj_center_x = w / 2;
                        obj_center_y = h / 2;
                        */

                        //obj_range = obj_range * 2;

                        obj_center_x = (LeftTop_x + RightBottom_x) / 2;
                        obj_center_y = (LeftTop_y + RightBottom_y) / 2;
                        object_width = abs(RightTop_x - LeftTop_x);
                        object_height = abs(RightTop_y - RightBottom_y);
                }
        }
        else {
                /*
                radius = w/2;
                circle_x = w / 2;
                circle_y = h / 2;
                */
                /*
                obj_center_x = w / 2;
                obj_center_y = h / 2;
                object_width = square_width;
                object_height = square_height;
                 */
                obj_center_x = (LeftTop_x + RightBottom_x) / 2;
                obj_center_y = (LeftTop_y + RightBottom_y) / 2;
                object_width = abs(RightTop_x - LeftTop_x);
                object_height = abs(RightTop_y - RightBottom_y);
        }
        //imshow("Live", frame);
        matResult = frame;
        //return result_num;

}

extern "C" JNIEXPORT void JNICALL
Java_life_knowledge4_videotrimmersample_Brother_getJNIString(JNIEnv *env, jobject obj) {
        IplImage *image = 0;
        CvCapture *capture = cvCaptureFromCAM(0);
        cvNamedWindow("T9-camera", CV_WINDOW_AUTOSIZE);
        //cvResizeWindow("T9-camera", 320, 240);

        //cvGrabFrame(capture);
        //image = cvRetrieveFrame(capture);

        //cvShowImage("T9-camera", image);
}