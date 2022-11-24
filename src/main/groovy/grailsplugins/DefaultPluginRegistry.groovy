package grailsplugins

import grailsplugins.config.PluginRegistryConfig
import grailsplugins.models.GrailsPlugin
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Singleton
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Slf4j
@Singleton
@CompileStatic
class DefaultPluginRegistry implements PluginRegistry {

    public static final DateTimeFormatter UTC = DateTimeFormatter.ISO_INSTANT.withZone ZoneId.of('UTC')
    private static final String QUERY_PREFIX_OWNER = 'owner:'
    private static final String QUERY_PREFIX_LABEL = 'label:'

    private final PluginRegistryConfig config
    private final Map<PluginId, GrailsPlugin> grailsPlugins = Collections.synchronizedMap(new LinkedHashMap())
    private final Map<PluginId, GrailsPlugin> backup = Collections.synchronizedMap(new LinkedHashMap())
    private final Map<PluginId, String> currentVersions = Collections.synchronizedMap(new LinkedHashMap())

    DefaultPluginRegistry(PluginRegistryConfig config) {
        this.config = config
    }

    @Override
    void backup() {
        backup.clear()
        backup.putAll grailsPlugins
    }

    @Override
    void restoreBackup() {
        if(backup.size() > 0) {
            grailsPlugins.putAll backup
            backup.clear()
        }
    }

    @Override
    void clearStalePlugins() {

        if(grailsPlugins) {

            def freshAfterLimit = ZonedDateTime.now().minus config.staleAfterMinutes, ChronoUnit.MINUTES

            // Get the keys of the plugins that have been in the registry for some amount of time
            def keysToBeRemoved = grailsPlugins.collectMany { k, v ->
                v.lastUpdatedInRegistry.isBefore(freshAfterLimit) ? [k] : []
            }
            if(keysToBeRemoved) {
                // Remove the plugins that match the above criteria
                log.info 'Removing {} plugins not updated since {}', keysToBeRemoved.size(), freshAfterLimit.format(UTC)
                keysToBeRemoved.each {
                    grailsPlugins.remove it
                    currentVersions.remove it
                }
            }
        }
    }

    @Override
    void savePlugin(GrailsPlugin plugin) {
        def id = PluginId.of plugin
        plugin.lastUpdatedInRegistry = ZonedDateTime.now()
        grailsPlugins.put id, plugin
        currentVersions.put id, plugin.latestVersion
    }

    @Override
    GrailsPlugin findPlugin(PluginId id) {
        grailsPlugins[id]
    }

    @Override
    int getTotalPluginCount() {
        grailsPlugins.size()
    }

    @Override
    Collection<GrailsPlugin> getTopRatedPlugins() {
        grailsPlugins
            .values()
            .findAll { it.githubRepo?.stargazersCount }
            .sort { a, b -> b.githubRepo.stargazersCount <=> a.githubRepo.stargazersCount }
            .take(config.numberOfTopRatedPlugins)
    }

    @Override
    Collection<GrailsPlugin> getLatestPlugins() {
        grailsPlugins
            .values()
            .findAll {it.updated }
            .sort { a, b -> ZonedDateTime.parse(b.updated, UTC) <=> ZonedDateTime.parse(a.updated, UTC) }
            .take(config.numberOfLatestPlugins)
    }

    @Override
    Collection<GrailsPlugin> getAllPlugins() {
        grailsPlugins
            .values()
            .sort { a, b -> a.name.toLowerCase() <=> b.name.toLowerCase() }
    }

    @Override
    Collection<GrailsPlugin> findPluginsByQuery(String query) {

        if(!query) return Collections.emptyList()

        Collection<GrailsPlugin> plugins

        if (query.startsWithIgnoreCase QUERY_PREFIX_OWNER) {
            def owner = query.substring QUERY_PREFIX_OWNER.length()
            plugins = findPluginsByOwner owner
        }
        else if (query.startsWithIgnoreCase QUERY_PREFIX_LABEL) {
            def label = query.substring QUERY_PREFIX_LABEL.length()
            plugins = findPluginsByLabel label
        }
        else {
            plugins = grailsPlugins
                .values()
                .findAll {
                    def name = it.name.toLowerCase()
                    def desc = it.description?.toLowerCase()
                    // Is the query in the plugin name (with spaces instead of hyphens)?
                    (name.replace((char) '-', (char) ' ').containsIgnoreCase(query)) ||
                    // Or, is the query in the reqular name?
                    (name.containsIgnoreCase(query)) ||
                    // Or, is the query in the description
                    (desc?.containsIgnoreCase(query)) ||
                    // Or, is the query in any of the plugins labels
                    (it.labels.any { it.containsIgnoreCase(query) })
                }
        }
        plugins
    }

    @Override
    GrailsPlugin findPluginByName(String name) {
        grailsPlugins
            .values()
            .find { it.name.equalsIgnoreCase name }
    }

    @Override
    Collection<GrailsPlugin> findPluginsByOwner(String owner) {
        grailsPlugins
            .values()
            .findAll {it.owner.equalsIgnoreCase owner }
    }

    Collection<GrailsPlugin> findPluginsByLabel(String label) {
        grailsPlugins
            .values()
            .findAll { GrailsPlugin plugin -> plugin.labels.any {it.equalsIgnoreCase label }
        }
    }

    @Override
    String findPreviousVersionOf(GrailsPlugin plugin) {
        currentVersions.get(PluginId.of plugin)
    }
}