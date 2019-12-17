(ns cartagena.renderers-test
  (:require [clojure.test :refer :all]
            [cartagena.renderers :refer :all]))

; TODO: re-enable this tests and modify them so they actually test something
; (now just trigger the side effects)

; (deftest calculate-join-position-test
;   (testing "calculate-join-position"
;     (is (= 61
;     	(calculate-join-position [{
;           :pieces {:green 6, :red 6, :yellow 6}, :type :start} {
;             :pieces {:green 0, :red 0, :yellow 0}, :type :flag} {
;             :pieces {:green 0, :red 0, :yellow 0}, :type :pistol} {
;             :pieces {:green 0, :red 0, :yellow 0}, :type :bottle} {
;             :pieces {:green 0, :red 0, :yellow 0}, :type :hat} {
;             :pieces {:green 0, :red 0, :yellow 0}, :type :keys} {
;             :pieces {:green 0, :red 0, :yellow 0}, :type :sword}])))))


; (deftest render-section-test
;   (testing "render-section"
;     (is (= nil
;     	(render-section [{
;           :pieces {:green 6, :red 6, :yellow 6}, :type :start} {
;             :pieces {:green 0, :red 0, :yellow 0}, :type :flag} {
;             :pieces {:green 0, :red 0, :yellow 0}, :type :pistol} {
;             :pieces {:green 0, :red 0, :yellow 0}, :type :bottle} {
;             :pieces {:green 0, :red 0, :yellow 0}, :type :hat} {
;             :pieces {:green 0, :red 0, :yellow 0}, :type :keys} {
;             :pieces {:green 0, :red 0, :yellow 0}, :type :sword}])))))

; (deftest render-join-test
;   (testing "render-join"
;     (is (= nil
;     	(render-join 61)))))
 