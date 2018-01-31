<g:if test="${plugin.githubRepository}">
    <div class="githubstar">
        <a href="${plugin.githubRepository.htmlUrl}/stargazers">
            <span class="star"><asset:image src="small_githubstar.svg" height="20"/></span>
            <span class="count">${plugin.githubRepository.stargazersCount}</span>
        </a>
    </div>
</g:if>
