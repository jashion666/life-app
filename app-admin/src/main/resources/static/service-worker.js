const version = 1;
const CACHE_NAME = 'stale-' + version;
let hasSaved = false;
let savedResult;

self.addEventListener('install', function (event) {
    event.waitUntil(
        preCache().then(self.skipWaiting())
    );
});

function preCache() {
    return caches.open(CACHE_NAME).then(function (cache) {
        return cache.addAll([
            '/',
            'index.html',
            'detail.html',
        ]);
    })
}

self.addEventListener('fetch', function (event) {
    const backUpRequest = event.request.clone();
    event.respondWith(
        fetch(event.request).then(function (response) {
            return caches.open(CACHE_NAME).then(function (cache) {
                if (response.status >= 500) {
                    return cache.match(event.request).then(function (response) {
                        return response;
                    })
                } else {
                    if (event.request.method !== 'POST') {
                        cache.put(event.request, response.clone());
                    }
                    return response;
                }
            });
        }).catch(function (error) {
            console.log('Fetch failed. Returning offline page instead.', error);

            // 一览查询接口处理
            if (event.request.url.indexOf("test") > 0) {
                console.log('一览查询接口处理');
                let res = getIndexList(caches);
                return res.then(function (searchResponse) {
                    putCaches(caches, event.request, searchResponse.clone());
                    return searchResponse;
                });
            }
            // 详细画面查询接口处理
            if (event.request.url.indexOf("search") > 0) {
                console.log('详细画面查询接口处理');
                return getSearchResponse(event.request, caches);
            }
            // 详细画面更新接口处理
            if (event.request.url.indexOf("update") > 0) {
                console.log('详细画面更新接口处理');
                return saveDetail(backUpRequest, caches);
            }
            // 单纯html缓存获得
            const html = getHtml(event.request.url);
            if (html) {
                console.log('单纯html缓存获得');
                return caches.match(html);
            }
            // 其他非特定缓存获得
            return caches.match(event.request).then(function (response) {
                console.log('其他非特定缓存获得');
                return response;
            });
        })
    );
});

/**
 * 一览接口处理
 */
async function getIndexList(caches) {
    let result = await getCaches(caches, "test");
    changeIndexList(result);
    return createResponse(result);
}

/**
 * 单纯html缓存获得
 */
function getHtml(url) {
    if (url.indexOf(".html") > 0) {
        url = url.substring(url.lastIndexOf("/") + 1);
        if (url.indexOf("?")) {
            url = url.substring(0, url.lastIndexOf("?"));
        }
        return url;
    }
    return null;
}

/**
 * 详细画面登录处理
 */
async function saveDetail(request) {
    console.log('保存详细画面的数据');
    savedResult = await getSaveData(request);
    hasSaved = true;
    return createResponse(savedResult);
}

/**
 * 详细画面post数据取得
 */
function getSaveData(backUpRequest) {
    return new Promise(resolve => {
        backUpRequest.json().then(ret => {
            resolve(ret);
        });
    })
}

/**
 * 详细画面检索接口处理
 */
async function getSearchResponse(request, caches) {
    const result = await getCaches(caches, "test");
    return createResponse({reload: true, result});
}

/**
 * 一览画面更改数据
 */
function changeIndexList(result) {
    if (!hasSaved) {
        return;
    }
    for (const item of result) {
        if (item.homeCode === savedResult.homeCode) {
            item.name = savedResult.name;
            item.thisTime = savedResult.thisTime;
            item.lastTime = savedResult.lastTime;
            break;
        }
    }
    hasSaved = false;
    savedResult = {};
}

/**
 * 取得缓存
 */
function getCaches(caches, key) {
    return new Promise(resolve => {
        caches.match(key).then(response => {
            return response.clone().json();
        }).then(result => {
            resolve(result);
        });
    })
}

/**
 * 存入缓存
 */
async function putCaches(caches, key, value) {
    await doPut(caches, key, value)
}

/**
 * 存入缓存
 */
function doPut(caches, key, value) {
    return new Promise(resolve => {
        caches.open(CACHE_NAME).then(function (cache) {
            cache.put(key, value);
            resolve(key);
        });
    })
}

/**
 * mock 响应数据
 */
function createResponse(result) {
    const response = {
        body: result,
        init: {
            status: 200,
            statusText: 'OK',
            headers: {
                'Content-Type': 'application/json',
                'X-Mock-Response': 'yes'
            }
        }
    };
    return new Response(JSON.stringify(response.body), response.init);
}
