(ns servers "Clojure web server benchmarks"
  {:author "Peter Taoussanis"}
  (:require [clojure.string          :as str]
            [compojure.core          :as compojure]
            [ring.adapter.jetty      :as jetty]
            [ring.adapter.simpleweb  :as simpleweb]
            [ring.adapter.netty      :as netty]
            [lamina.core             :as lamina]
            [aleph.http              :as aleph]
            [aloha.core              :as aloha]
            [me.shenfeng.http.server :as http-kit]
            [taoensso.timbre :as timbre :refer (trace debug info warn error report)]
            [taoensso.timbre.profiling :as profiling :refer (p profile)]))

(defonce servers (atom {}))
(def response {:status 200 :headers {"content-type" "text/html"} :body "echo"})

;;;; Handlers

(defn handler             [request] response)
(defn aleph-handler-async [channel request] (lamina/enqueue channel response))
(def  aleph-handler-sync  (aleph/wrap-ring-handler handler))
(http-kit/defasync http-kit-async [request] cb (cb response))

;;;; Servers

(compojure/defroutes compojure-handler (compojure/GET "*" [] response))

(defn server
  [name & [port create-server-fn]]
  (if-let [server (get @servers name)]
    server
    (when (and port create-server-fn)
      (let [server (create-server-fn port)]
        (swap! servers assoc name server)
        (info name "is running on port" port)
        server))))

(def system
  (let [properties (into {} (System/getProperties))]
    (fn [& keys] (str/join " " (map properties keys)))))

(defn -main [& args]
  (info "Starting up servers..."
        {:clojure (clojure-version)
         :os      (system "os.name" "os.version")
         :java    (system "java.version")
         :vm      (system "java.vm.name" "java.vm.version")})

  (server :ring-jetty     8081 #(jetty/run-jetty handler {:join? false :port %}))
  (server :ring-simple    8082 #(simpleweb/run-simpleweb handler {:port %}))
  (server :aleph          8083 #(aleph/start-http-server aleph-handler-sync  {:port %}))
  (server :aleph-async    8084 #(aleph/start-http-server aleph-handler-async {:port %}))
  (server :aloha          8085 #(aloha/start-http-server handler {:port %}))
  (server :http-kit       8086 #(http-kit/run-server handler {:port %}))
  (server :http-kit-async 8087 #(http-kit/run-server http-kit-async {:port %}))
  (server :ring-netty     8088 #(netty/run-netty handler {:port % "reuseAddress" true})))

;;;; Results @ http://goo.gl/bgyVI (Macbook Air 1.7Ghz i5)
;; nginx 1.2.5     ; ~10,000 reqs/sec
;; :http-kit       ;  ~8,600 reqs/sec
;; :http-kit-async ;  ~8,600 reqs/sec
;; :aloha          ;  ~7,800 reqs/sec
;; :ring-netty     ;  ~7,300 reqs/sec
;; :ring-jetty     ;  ~6,100 reqs/sec
;; :ring-simple    ;  ~4,600 reqs/sec
;; :aleph          ;  ~2,800 reqs/sec
;; :aleph-async    ;  ~4,500 reqs/sec