(ns leiningen.sphinx
	(:require [clojure.string :as s]
						[clojure.java.shell :refer (sh)]))

(defn- tags-args
	"Output a string of tag arguments for the sequence"
	[tags]
	(flatten (map #(vec ["-t" (name %)]) tags)))

(defn- map-to-args
	"Output a string of tag arguments for the sequence"
	[flag arg-map]
	(flatten (map #(vec [flag  (str (name %) "=" (% arg-map))]) (keys arg-map))))

(defn- sphinx-opts
	"Turn the provided options into flags and apply the default options"
	[opts]
	(let [source (get opts :source "doc")
				additional (get opts :additional-options)]
		{
			:builder (name (get opts :builder :html))
			:source source
			:output (get opts :output (str source "/_build"))
			:config (get opts :config source)
			:rebuild (if (get opts :rebuild) "-a -E" nil)
			:nitpicky (if (get opts :nitpicky) "-n" nil)
			:warn-as-error (if (get opts :warn-as-error) "-W" nil)
			:tags (tags-args (get opts :tags []))
			:setting-values (map-to-args "-D" (get opts :setting-values {}))
			:html-template-values (map-to-args "-A" (get opts :html-template-values {}))
			:additional-options (if additional (s/split additional #"\s+") nil)}))

(defn- sphinx-command
	"Turn the options into a sphinx-build command"
	[project]
	(let [opts (sphinx-opts project)]
		(remove s/blank? (flatten [
			"sphinx-build"
			"-b" (:builder opts)
			"-c" (:config opts)
			(:rebuild opts)
			(:nitpicky opts)
			(:warn-as-error opts)
			(:tags opts)
			(:setting-values opts)
			(:html-template-values opts)
			(:additional-options opts)
			(:source opts)
			(:output opts)]))))

(defn- build-sphinx
	"Run sphinx-build with the provided configuration options"
	[opts]
	(let [command (sphinx-command opts)
  			result (apply sh command)]
  	(println "\nRunning Sphinx as:")
  	(println (s/join " " command) "\n\n")
  	(println (:out result))
  	(if-not (s/blank? (:err result)) (println (:err result)))))

(defn sphinx
  "Build Sphinx documentation"
  [project & args]
  (let [opts (:sphinx project)]
  	(cond 
  		(or (nil? opts) (empty? opts) (not-every? map? (vals opts)))
  			; there is just one configuration, so build it
  			(build-sphinx opts)
  		(nil? args)
  			; there are multiple configurations, but no arguments, so build all of them
  			(doseq [opt (vals opts)] (build-sphinx opt))
  		:else
  			; build once for each argument that corresponds to a configuration
  			(doseq [opt (map keyword args)] 
  				(if (opt opts)
  					(build-sphinx opt)
  					(println "\nNo configuration found for:" (name opt) "\n"))))))