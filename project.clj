(defproject gol "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring-server "0.3.0"]
                 [org.clojure/clojurescript "0.0-2268"]
                 [figwheel "0.1.3-SNAPSHOT"]
                 [com.cemerick/clojurescript.test "0.3.1"]]
  :plugins [[lein-ring "0.8.11"]
            [lein-cljsbuild "1.0.3"]
            [lein-figwheel "0.1.3-SNAPSHOT"]
            [com.cemerick/clojurescript.test "0.3.1"]
            [com.cemerick/austin "0.1.4"]]
  :figwheel {:http-server-root "public"
             :port 3449
             :css-dirs ["resources/public/css"]}
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/gol" "src/figwheel" "src/brepl"]
                        :compiler {:output-to "resources/public/js/main.js"
                                   :output-dir "resources/public/js"
                                   :optimizations :none
                                   :source-maps true
                                   :pretty-print true}}
                       {:id "test"
                        :source-paths ["src/gol" "test/gol"]
                        :compiler {:output-to "gol_test.js"
                                   :optimizations :whitespace}
                        :notify-command ["phantomjs" :cljs.test/runner "gol_test.js"]}]}
  :ring {:handler gol.handler/app})
