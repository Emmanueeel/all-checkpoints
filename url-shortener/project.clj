(defproject url-shortener "0.1.0-SNAPSHOT"
  :description "REST API для сокращения URL"
  :dependencies [[org.clojure/clojure "1.11.1"]
                  [ring/ring-core "1.10.0"]
                  [ring/ring-jetty-adapter "1.10.0"]
                  [ring/ring-json "0.5.1"]
                  [compojure "1.7.0"]
                  [korma "0.4.3"]
                  [org.xerial/sqlite-jdbc "3.41.0.0"]
                  [clj-http "3.12.3"]
                  [org.clojure/tools.cli "1.0.219"]]
  :main ^:skip-aot url-shortener.core
  :uberjar-name "url-shortener-0.1.0-standalone.jar"
  :profiles {:uberjar {:aot :all}})
