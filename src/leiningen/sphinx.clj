(ns leiningen.sphinx
	(:require [clojure.string :as s]
						[clojure.java.shell :refer (sh)]))

(defn- check-opts
	"Make sure the supplied options are valid"
	[opts]
	opts)

(defn- tags-args
	"Output a string of tag arguments for the sequence"
	[tags]
	(flatten (map #(vec ["-t" (name %)]) tags)))

(defn- map-to-args
	"Output a string of tag arguments for the sequence"
	[flag arg-map]
	(flatten (map #(vec [flag  (str (name %) "=" (% arg-map))]) (keys arg-map))))

(defn- sphinx-opts
	"Apply the default options"
	[project]
	(check-opts 
		(let [opts (:sphinx project)
					source (get opts :source "doc")]
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
				:html-template-values (map-to-args "-A" (get opts :html-template-values {}))})))

(defn- opts-to-command
	"Turn the options into a sphinx-build command"
	[project]
	(let [opts (check-opts (sphinx-opts project))]
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
			(:source opts)
			(:output opts)]))))

(defn sphinx
  "Build Sphinx documentation"
  [project & args]
  (let [command (opts-to-command project)
  			result (apply sh command)]
  	(println "\nRunning Sphinx as:")
  	(println (s/join " " command) "\n\n")
  	(println (:out result))
  	(if-not (= 0 (:exit result)) (println (:err result)))))