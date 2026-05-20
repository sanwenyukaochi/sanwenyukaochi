Gitflow 工作流程
============

Gitflow 是一种传统的 Git 工作流程，最初是一种颠覆性的新颖策略，用于管理 Git 分支。Gitflow 越来越受欢迎，支持[基于主干的工作流程](https://www.atlassian.com/zh/continuous-delivery/continuous-integration/trunk-based-development)，这些工作流程现在被认为是现代持续软件开发和 [DevOps](https://www.atlassian.com/zh/devops/what-is-devops) 实践的最佳实践。将 Gitflow 与 [CI/CD](https://www.atlassian.com/zh/continuous-delivery) 一起使用也可能具有挑战性。出于历史目的，这篇文章详细介绍了 Gitflow。

什么是 Gitflow？
------------

Gitflow 是一种替代的 Git 分支模型，它使用功能分支和多个主分支。它最初由 [Vincent Driessen 在 nvie](http://nvie.com/posts/a-successful-git-branching-model/) 发布并广受欢迎。与基于主干的开发相比，Gitflow 有许多寿命更长的分支和更大的提交。在此模型下，开发人员创建一个功能分支并将其合并到主干分支直到该功能完成。这些长期的功能分支需要更多的协作才能合并，因为它们偏离主干分支并引入冲突更新的风险更高。他们还可能引入冲突的更新。

Gitflow 可用于有预定发布周期的项目，也可用于[持续交付](https://www.atlassian.com/zh/devops/what-is-devops/devops-best-practices)的 [DevOps 最佳实践](https://www.atlassian.com/zh/continuous-delivery)。除了[功能分支工作流程](https://www.atlassian.com/zh/git/tutorials/comparing-workflows/feature-branch-workflow)所需的概念或命令外，此工作流程不会添加任何新概念或命令。相反，它为不同的分支分配了非常具体的角色，并定义了它们应该如何以及何时进行交互。除了 `feature` 分支外，它还使用单独分支来准备、维护和录制版本。当然，您还可以利用功能分支工作流程的所有优势：拉取请求、隔离实验和更高效的协作。

工作原理
----

![Git 工作流程](https://dam-cdn.atl.orangelogic.com/AssetLink/3we6dfk04vp4y2f0f2188t6j13n4424h.svg)

### develop 和 main 分支

此工作流程不是单个 `main` 分支，而是使用两个分支来记录项目的历史记录。`main` 分支存储官方版本历史记录，`develop` 分支充当功能的集成分支。使用版本号标记 `main` 分支中的所有提交也很方便。

第一步是用 `develop` 分支来补充默认的 `main` 分支。一个简单的方法是让一个开发人员在本地创建一个空的 `develop` 分支并将其推送到服务器：

    1git branch develop
    2git push -u origin develop

该分支将包含项目的完整历史记录，而 `main` 分支将包含删节版本。其他开发人员现在应该克隆中央存储库并创建一个用于 `develop` 的跟踪分支。

使用 git-flow 扩展库时，在现有代码存储库上执行 `git flow init` 将创建 `develop` 分支：

    1$ git flow init
    2
    3Initialized empty Git repository in ~/project/.git/
    4No branches exist yet. Base branches must be created now.
    5Branch name for production releases: [main]
    6Branch name for "next release" development: [develop]
    7
    8How to name your supporting branch prefixes?
    9Feature branches? [feature/]
    10Release branches? [release/]
    11Hotfix branches? [hotfix/]
    12Support branches? [support/]
    13Version tag prefix? []
    14
    15$ git branch
    16* develop
    17 main

feature 分支
----------

### 步骤 1. 创建代码库

每项新功能都应位于自己的分支中，可以将其[推送到中央存储库](https://www.atlassian.com/zh/git/tutorials/syncing/git-push)进行备份/协作。但是，`feature` 分支不是从 `main` 分支中分支，而是使用 `develop` 作为其父分支。一个 feature 完成后，就会[合并回 develop 中](https://www.atlassian.com/zh/git/tutorials/using-branches/git-merge)。feature 不应直接与 `main` 交互。

![Git 工作流 - 功能分支](https://dam-cdn.atl.orangelogic.com/AssetLink/7yu4h3oai315s158nq6t313p25e82o32.svg)

请注意，无论出于何种意图和目的，`feature` 分支与 `develop` 分支相结合都是功能分支工作流。但是，Gitflow 工作流并不止于此。

`feature` 分支通常是在最新的 `develop` 分支基础上创建的。

### 创建 feature 分支

没有 git-flow 扩展：

    1git checkout develop
    2git checkout -b feature_branch

使用 git-flow 扩展时：

    1git flow feature start feature_branch

继续工作，像往常一样使用 Git。

### 完成 feature 分支

当您完成该功能的开发工作后，下一步是将 `feature_branch` 合并到 `develop`中。

没有 git-flow 扩展：

    1git checkout develop
    2git merge feature_branch

使用 git-flow 扩展：

    1git flow feature finish feature_branch

release 分支
----------

![Git 工作流 - release 分支](https://dam-cdn.atl.orangelogic.com/AssetLink/le16ot34d0e862mba18e7j7i502585eb.svg)

一旦 `develop` 获得了足够的功能来发布某个版本（或者预先确定的发布日期即将到来），您就可以从 `develop` 中分离出一个 `release` 分支。创建此分支将启动下一个发布周期，因此在此之后无法添加任何新功能——只有错误修复、文档生成和其他面向发布的任务才能进入该分支。一旦准备好发布，`develop` 分支就会合并到 `main` 分支中，并用版本号进行标记。此外，它应该合并回 `develop` 中，该分支自发布以来可能已经取得了进展。

使用专门的分支来准备发布可以让一个团队完善当前版本，而另一个团队可以继续为下一个版本开发功能。它还创建了定义明确的开发阶段（例如，很容易说 “本周我们正在为版本 4.0 做准备”，也可以在存储库的结构中真正看到它）。

创建 `release` 分支是另一个简单直接的分支操作。与 `feature` 分支一样，`release` 分支也基于 `develop` 分支。可以使用以下方法创建新的 `release` 分支。

没有 git-flow 扩展：

    1git checkout develop
    2git checkout -b release/0.1.0

使用 git-flow 扩展时：

    1$ git flow release start 0.1.0
    2Switched to a new branch 'release/0.1.0'

一旦发布准备好交付，它将会合并到 `main` 和 `develop` 分支，然后 `release` 分支将被删除。重新合并到 `develop` 中很重要，因为关键更新可能已添加到 `release` 分支中，并且需要可以访问新功能。如果您的组织强调代码审查，那么这将是提交拉取请求的理想场所。

要完成 `release` 分支，请使用以下方法：

没有 git-flow 扩展：

    1git checkout main
    2git merge release/0.1.0

或使用 git-flow 扩展：

    1git flow release finish '0.1.0'

hotfix 分支
---------

![Git 工作流中的 hotfix 分支](https://dam-cdn.atl.orangelogic.com/AssetLink/t8b1bnptx6bn40wc43g83j02u5b61064.svg)

维护或 `“hotfix”` 分支用于快速修补生产版本。`hotfix` 分支与 `release` 分支和 `feature` 分支很像，不同的是它们是基于 `main` 而不是 `develop` 分支。这是唯一一个应该直接从 `main` 克隆的分支。修复完成后，应将其合并到 `main` 和 `develop`（或当前 `release` 分支）中，并应使用更新的版本号标记 `main`。

拥有专门的错误修复开发线可以让您的团队在不中断工作流程其余部分或等待下一个发布周期的情况下解决问题。您可以将维护分支视为直接与 `main` 配合使用的临时 `release` 分支。可以使用以下方法创建 `hotfix` 分支：

没有 git-flow 扩展：

    1git checkout main
    2git checkout -b hotfix_branch

使用 git-flow 扩展时：

    1$ git flow hotfix start hotfix_branch

与完成 `release` 分支类似，`hotfix` 分支会合并到 `main` 和 `develop` 中。

    1git checkout main
    2git merge hotfix_branch
    3git checkout develop
    4git merge hotfix_branch
    5git branch -D hotfix_branch

    1$ git flow hotfix finish hotfix_branch

示例
--

演示功能分支流程的完整示例如下所示。假设我们有一个带 `main` 分支的代码存储库设置。

    1git checkout main
    2git checkout -b develop
    3git checkout -b feature_branch
    4# work happens on feature branch
    5git checkout develop
    6git merge feature_branch
    7git checkout main
    8git merge develop
    9git branch -d feature_branch

除了 `feature` 和 `release` 流程外，还有一个 `hotfix` 示例，如下所示：

    1git checkout main
    2git checkout -b hotfix_branch
    3# work is done commits are added to the hotfix_branch
    4git checkout develop
    5git merge hotfix_branch
    6git checkout main
    7git merge hotfix_branch

摘要
--

这里我们讨论了 Gitflow 工作流程。Gitflow 是您和团队可以使用的众多 [Git 工作流程](https://www.atlassian.com/zh/git/tutorials/comparing-workflows)之一。

关于 Gitflow 需要了解的一些关键要点是：

*   该工作流程非常适合基于版本的软件工作流程。

*   Gitflow 为生产 hotfix 提供专用渠道。


Gitflow 的总体流程是：

1.  `develop` 分支是从 `main` 中创建的

2.  `release` 分支是从 `develop` 中创建的

3.  `feature` 分支是从 `develop` 中创建的

4.  `feature` 完成后，它会合并到 `develop` 分支中

5.  `release` 分支完成后，它将合并到 `develop` 和 `main` 中

6.  如果在 `main` 中检测到问题，则会从 `main` 创建 `hotfix` 分支

7.  `hotfix` 完成后，它将合并到 `develop` 和 `main` 中

