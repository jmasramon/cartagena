(ns cartagena.swingUI.drawing-test
  (:require [clojure.test :refer :all]
            [cartagena.swingUI.drawing :refer :all]))

(def fullGameState {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                            {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}],
                    :turn-order {:green :red, :red :yellow, :yellow :green}
                    :turn :green
                    :board [
                        {:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                        {:pieces {:green 1, :red 2, :yellow 3}, :type :bottle}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                    :deck [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]})

