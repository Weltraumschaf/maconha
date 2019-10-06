(defproject web-search "1.0.0-SNAPSHOT"
  :description "Web UI for Maconha search."
  :url "https://github.com/weltraumschaf/maconha"
  :license {:name "\"THE BEER-WARE LICENSE\" (Revision 43)"
            :url "https://www.weltraumschaf.de/the-beer-ware-license.txt"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-jetty-adapter "1.6.1"]
                 [hiccup "1.0.5"]]
  :plugins [[lein-ring "0.12.5"]]
  :main my-webapp.handler
  :ring {:handler web-search.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
