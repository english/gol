(ns gol.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.server.standalone :refer [serve]]
            [ring.util.response :refer [file-response]]))

(defroutes app-routes
  (GET "/" [] (file-response "resources/public/index.html"))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app (handler/site app-routes))

(defonce server (atom nil))

(defn get-handler []
  (-> #'app
    (wrap-file "resources")
    (wrap-file-info)))

(defn start-server
  "used for starting the server in development mode from REPL"
  []
  (reset! server
          (serve (get-handler)
                 {:port 3000
                  :open-browser? false
                  :auto-reload? true
                  :join true})))

(defn stop-server []
  (.stop @server)
  (reset! server nil))
