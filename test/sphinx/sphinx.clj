(ns sphinx.sphinx
  (:require [midje.sweet :refer :all]
  					[midje.util :refer (testable-privates)]
  					[clojure.string :as s]
  					[leiningen.sphinx]))

(testable-privates leiningen.sphinx opts-to-command)

(defn- map-to-command [opts-map]
	(s/join " " (opts-to-command opts-map)))

(facts "about valid configurations generating a build-sphinx command"

	(fact "there is a resonable default when no options are provided"
		(map-to-command {}) => "sphinx-build -b html -c doc doc doc/_build")

	(fact "specifying the source option includes the correct source and output directories"
		(map-to-command {:sphinx {:source "foo"}}) => "sphinx-build -b html -c foo foo foo/_build")

	(fact "specifying the ouput option includes the correct output directory"
		(map-to-command {:sphinx {:output "foo/HTML"}}) => "sphinx-build -b html -c doc doc foo/HTML")

	(fact "specifying the rebuild option includes the rebuild flags"
		(map-to-command {:sphinx {:rebuild true}}) => "sphinx-build -b html -c doc -a -E doc doc/_build")

	(fact "specifying the nitpicky option includes the nitpicky flag"
		(map-to-command {:sphinx {:nitpicky true}}) => "sphinx-build -b html -c doc -n doc doc/_build")

	(fact "specifying the warn-as-error option includes the warn flag"
		(map-to-command {:sphinx {:warn-as-error true}}) => "sphinx-build -b html -c doc -W doc doc/_build")

	(fact "specifying tags includes tags arguments"
		(map-to-command {:sphinx {:tags []}}) => "sphinx-build -b html -c doc doc doc/_build"
		(map-to-command {:sphinx {:tags [:foo]}}) =>
			"sphinx-build -b html -c doc -t 'foo' doc doc/_build"
		(map-to-command {:sphinx {:tags [:foo :bar]}}) =>
			"sphinx-build -b html -c doc -t 'foo' -t 'bar' doc doc/_build"
		(map-to-command {:sphinx {:tags [:foo :bar :blat]}}) =>
			"sphinx-build -b html -c doc -t 'foo' -t 'bar' -t 'blat' doc doc/_build")

	(fact "specifying settings values includes settings arguments"
		(map-to-command {:sphinx {:setting-values {}}}) => "sphinx-build -b html -c doc doc doc/_build"
		(map-to-command {:sphinx {:setting-values {:pygments_style "solarizedlight"}}}) =>
			"sphinx-build -b html -c doc -D pygments_style='solarizedlight' doc doc/_build"
		(let [command (map-to-command {:sphinx {:setting-values 
				{:pygments_style "solarizedlight"
				:html_theme_options.linkcolor "#B86644"}}})]
			command => (has-prefix "sphinx-build -b html -c doc ")
			command => (contains " -D pygments_style='solarizedlight' ")
			command => (contains " -D html_theme_options.linkcolor='#B86644' ")
			command => (has-suffix " doc doc/_build"))
		(let [command (map-to-command {:sphinx {:setting-values
				{:pygments_style "solarizedlight"
				 :html_theme_options.linkcolor "#B86644"
				 :html_theme_options.visitedlinkcolor "#B86644"}}})]
			command => (has-prefix "sphinx-build -b html -c doc ")
			command => (contains " -D pygments_style='solarizedlight' ")
			command => (contains " -D html_theme_options.linkcolor='#B86644' ")
			command => (contains " -D html_theme_options.visitedlinkcolor='#B86644' ")
			command => (has-suffix " doc doc/_build")))

	(fact "specifying html template values includes html template arguments"
		(map-to-command {:sphinx {:html-template-values {}}}) => "sphinx-build -b html -c doc doc doc/_build"
		(map-to-command {:sphinx {:html-template-values {:author "Albert Camus"}}}) =>
			"sphinx-build -b html -c doc -A author='Albert Camus' doc doc/_build"
		(let [command (map-to-command {:sphinx {:html-template-values 
				{:author "Albert Camus"
				 :mascot "Fighting Ferret"}}})]
			command => (has-prefix "sphinx-build -b html -c doc ")
			command => (contains " -A author='Albert Camus' ")
			command => (contains " -A mascot='Fighting Ferret' ")
			command => (has-suffix " doc doc/_build"))
		(let [command (map-to-command {:sphinx {:html-template-values 
				{:author "Albert Camus"
				 :mascot "Fighting Ferret"
				 :publication "Combat"}}})]
			command => (has-prefix "sphinx-build -b html -c doc ")
			command => (contains " -A author='Albert Camus' ")
			command => (contains " -A mascot='Fighting Ferret' ")
			command => (contains " -A publication='Combat' ")
			command => (has-suffix " doc doc/_build")))

	(fact "a complete simple configuration generates the right command"
		(map-to-command {:sphinx {
												:builder :dirhtml
												:source "API/REST"}}) =>
			"sphinx-build -b dirhtml -c API/REST API/REST API/REST/_build")

	(fact "a complete complex configuration generates the right command"
		(let [command (map-to-command
			{:sphinx {
				:builder :singlehtml
				:source "docs"
				:output "docs/HTML"
				:config "."
				:rebuild true
				:tags [:html, :draft]
				:nitpicky true
				:warn-as-error true
				:setting-values {
					:pygments_style "solarizedlight"
					:html_theme_options.linkcolor "#B86644"
					:html_theme_options.visitedlinkcolor "#B86644"
				}
				:html-template-values {
					:author "Albert Camus"
					:mascot "Fighting Ferret"
				}
			}})]
			command => (has-prefix "sphinx-build -b singlehtml -c . -a -E -n -W -t 'html' -t 'draft'")
			command => (contains " -D pygments_style='solarizedlight' ")
			command => (contains " -D html_theme_options.linkcolor='#B86644' ")
			command => (contains " -D html_theme_options.visitedlinkcolor='#B86644' ")
			command => (contains " -A author='Albert Camus' ")
			command => (contains " -A mascot='Fighting Ferret' ")
			command => (has-suffix " docs docs/HTML")))


(facts "about invalid configurations providing errors"))
