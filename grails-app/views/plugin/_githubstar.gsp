<g:if test="${plugin.githubRepo}">
    <div class="githubstar">
        <a href="${plugin.githubRepo.htmlUrl}/stargazers">
            <span class="star"><asset:image src="small_githubstar.svg" height="20"/></span>
            <span class="count">${plugin.githubRepo.stargazersCount}</span>
        </a>
    </div>
</g:if>
