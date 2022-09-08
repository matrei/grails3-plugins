package grailsplugins

import com.bintray.BintrayPackage
import com.github.GithubRepository
import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import java.text.DateFormat
import java.text.SimpleDateFormat
import groovy.transform.Synchronized

@Slf4j
@CompileStatic
class GrailsPluginsRepositoryService implements GrailsPluginsRepository, GrailsConfigurationAware {

    public static final DateFormat UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    int numberOfLatestGuides
    int numberOfTopRatedGuides

    @Override
    void setConfiguration(Config co) {
        numberOfLatestGuides = co.getProperty('grails.plugins.latestGuides.max', Integer, 5)
        numberOfTopRatedGuides = co.getProperty('grails.plugins.topRated.max', Integer, 5)
    }

    Map<BintrayKey, GrailsPlugin> grailsPlugins = [:]
    Map<BintrayKey, String> versions = [:]

    @Override
    @Synchronized
    void clearNotUpdatedSince(Date oneDayAgo) {
        Set<BintrayKey> keysToBeRemoved = []
        for ( BintrayKey key : grailsPlugins.keySet() ) {
            GrailsPlugin grailsPlugin = grailsPlugins[key]
            if ( grailsPlugin.lastUpdated && grailsPlugin.lastUpdated.before(oneDayAgo) ) {
                keysToBeRemoved << key
            }
        }
        log.info('removing #{}',"${keysToBeRemoved.size()}".toString())
        for ( BintrayKey key : keysToBeRemoved ) {
            grailsPlugins.remove(key)
            versions.remove(key)
        }
    }

    /**
     * @deprecated Use {@link #save(grailsplugins.BintrayKey, grailsplugins.GrailsPlugin)} instead.
     */
    @Deprecated
    @Synchronized
    @Override
    BintrayKey save(BintrayPackage bintrayPackage) {
        BintrayKey key = BintrayKey.of(bintrayPackage)
        GrailsPlugin grailsPlugin = new GrailsPlugin(bintrayPackage: bintrayPackage)
        grailsPlugin.lastUpdated = new Date()
        grailsPlugins.put(key, grailsPlugin)
        versions[key] = bintrayPackage.latestVersion

        key
    }

    @Synchronized
    @Override
    BintrayKey save(BintrayKey key, GrailsPlugin plugin) {
        grailsPlugins.put(key, plugin)
        plugin.lastUpdated = new Date()
        versions[key] = plugin.bintrayPackage.latestVersion
        key
    }

    @Synchronized
    @Override
    BintrayKey updateGithubRepository(BintrayKey key, GithubRepository githubRepository) {
        grailsPlugins[key]?.githubRepository = githubRepository
        key
    }

    @Synchronized
    @Override
    BintrayKey updateGithubRepositoryReadme(BintrayKey key, String readme) {
        grailsPlugins[key]?.readme = readme
        key
    }

    @Override
    GrailsPlugin find(BintrayKey key) {
        grailsPlugins[key]
    }

    @Override
    int count() {
        grailsPlugins.values().size()
    }

    @Override
    List<GrailsPlugin> topRatedPlugins() {
        grailsPlugins.values().findAll { GrailsPlugin  plugin ->
            plugin.githubRepository?.stargazersCount
        }.sort { GrailsPlugin a, GrailsPlugin b ->
            Integer aStarCount = a.githubRepository?.stargazersCount
            Integer bStarCount = b.githubRepository?.stargazersCount
            bStarCount <=> aStarCount
        }.take(numberOfTopRatedGuides)
    }

    @Override
    List<GrailsPlugin> latestPlugins() {
        grailsPlugins.values().findAll { GrailsPlugin plugin ->
            plugin.bintrayPackage?.updated
        }.sort { GrailsPlugin a, GrailsPlugin b ->
            Date aDate = UTC.parse(a.bintrayPackage?.updated)
            Date bDate = UTC.parse(b.bintrayPackage?.updated)
            bDate <=> aDate
        }.take(numberOfLatestGuides)
    }

    @Override
    List<GrailsPlugin> findAll() {
        grailsPlugins.values().sort { GrailsPlugin a, GrailsPlugin b ->
            a.bintrayPackage?.name?.toLowerCase() <=> b.bintrayPackage?.name?.toLowerCase()
        } as List<GrailsPlugin>
    }

    @Override
    List<GrailsPlugin> findByQuery(String query) {
        if ( query ) {
            String ownerPreffix = 'owner:'
            if ( query.startsWith(ownerPreffix) ) {
                String owner = query.substring(ownerPreffix.length())
                return grailsPlugins.values().findAll { GrailsPlugin plugin ->
                    plugin?.bintrayPackage?.owner == owner
                } as List<GrailsPlugin>
            }
            String labelPreffix = 'label:'
            if ( query.startsWith(labelPreffix) ) {
                String label = query.substring(labelPreffix.length())
                return grailsPlugins.values().findAll { GrailsPlugin plugin ->
                    plugin?.bintrayPackage?.labels?.contains(label)
                } as List<GrailsPlugin>
            }

            return grailsPlugins.values().findAll { GrailsPlugin plugin ->
                final String name = plugin.bintrayPackage?.name
                (name && name.replaceAll('-', ' ').toLowerCase().contains(query.toLowerCase())) ||
                        (name && name.toLowerCase().contains(query.toLowerCase())) ||
                        (plugin.bintrayPackage?.desc && plugin.bintrayPackage?.desc?.toLowerCase()?.contains(query.toLowerCase())) ||
                        (plugin.bintrayPackage?.labels?.any { String label -> label.toLowerCase().contains(query.toLowerCase()) })
            } as List<GrailsPlugin>
        }
        findAll()
    }

    @Override
    GrailsPlugin findByPluginName(String name) {
        grailsPlugins.values().find { GrailsPlugin plugin ->
            plugin.bintrayPackage?.name == name
        }
    }

    @Override
    String findPreviousLatestVersion(BintrayKey key) {
        versions[key]
    }
}
