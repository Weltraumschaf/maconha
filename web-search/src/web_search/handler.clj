(ns web-search.handler
  (:require
    [web-search.views :as views]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [ring.adapter.jetty :as jetty]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:gen-class))

(defroutes app-routes
           (GET "/" [] (views/home-page))
           (route/resources "/")
           (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

(defn -main [& args]
  (jetty/run-jetty #'app {:port  8080
                          :join? false}))