(defproject cartagena "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                [org.clojure/data.generators "0.1.2"]]
  :profiles {:dev {:plugins [	[com.jakemccrary/lein-test-refresh "0.14.0"]
  								[venantius/ultra "0.5.1"]]}}
  :source-paths ["src"]
  :main cartagena.swingUI.swingUI)
