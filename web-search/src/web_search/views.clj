(ns web-search.views
  (:require
    [hiccup.page :as page]))


(defn home-page
  []
  (page/html5
    [:head
     [:title "Maconha Search"]
     (page/include-css "/css/style.css")]
    [:body
     [:h1 "Maconha Search"]
     [:p "Hello, World!"]
     (page/include-js "/js/app.js")]))