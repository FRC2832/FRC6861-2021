import cv2
import numpy as np

capture = cv2.VideoCapture(1)
hue = [20, 77.8723404255319]
sat = [171.5296406942243, 255.0]
lum = [60, 246.29692832764508]

while True:
    ret, frame = capture.read()
    out = cv2.cvtColor(frame, cv2.COLOR_BGR2HLS)
    mask = cv2.inRange(out, (hue[0], lum[0], sat[0]),  (hue[1], lum[1], sat[1]))
    result = cv2.bitwise_and(frame, frame, mask = mask)
    result = cv2.blur(result, (2, 2))
    gray = cv2.cvtColor(result, cv2.COLOR_BGR2GRAY)
    circles = cv2.HoughCircles(gray, cv2.HOUGH_GRADIENT, 1.2, 50, maxRadius = 300)
    if circles is not None:
        circles = np.round(circles[0, :]).astype("int")
        for (x, y, r) in circles:
            cv2.circle(frame, (x, y), r, (0, 255, 0), 4)
    cv2.imshow("output", frame)
    cv2.imshow("mask", result)
    cv2.waitKey(25)