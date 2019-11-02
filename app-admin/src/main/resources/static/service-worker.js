const version = 1;
const CACHE_NAME = 'stale-' + version;

const indexUrl = 'index.html';

self.addEventListener('install', function (event) {
    self.skipWaiting();
    event.waitUntil(
        caches.open(CACHE_NAME).then(function (cache) {
            return cache.addAll([
                '/',
                'index.html',
                'detail.html',
            ]);
        })
    );
});

self.addEventListener('fetch', function (event) {
    // Always fetch response from the network
    event.respondWith(
        fetch(event.request).then(function (response) {
            console.log(event.request.url);
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
            console.log(event.request);
            console.log("detail.html");
            if (event.request.url.indexOf("search") > 0) {
                return getSearchResponse(event.request, caches);
            }
            return caches.match("detail.html");
            // return caches.match(event.request).then(function (response) {
                // if (event.request.url.indexOf("test") > 0) {
                //     response.clone().json().then(event => {
                //         localStorage.setItem("testData", JSON.stringify(event));
                //     })
                // } else {
                //     localStorage.removeItem("isTest");
                //     localStorage.removeItem("testData");
                // }
            //     console.log(response);
            //     return response;
            // });
        })
    );
});

async function getSearchResponse(request, caches) {
    const result = await getPersonList(caches);
    console.log(result);
    const response = {
        body: {
            result,
            "reload": true,
        },
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

function getPersonList(caches) {
    return new Promise(resolve => {
        caches.match("test").then(response => {
            return response.clone().json();
        }).then(result => {
            resolve(result);
        });
    })
}

// self.addEventListener('fetch', function (event) {
//     // request.mode = navigate isn't supported in all browsers
//     // so include a check for Accept: text/html header.
//     if (event.request.mode === 'navigate' ||
//         (event.request.method === 'GET' &&
//             event.request.headers.get('accept').includes('text/html'))) {
//         event.respondWith(
//             fetch(createCacheBustedRequest(event.request.url)).catch(function (error) {
//                 // Return the offline page
//                 console.log('Fetch failed. Returning offline page instead.', error);
//                 return caches.match(offlineUrl);
//             })
//         );
//     } else {
//         // Respond with everything else if we can
//         event.respondWith(caches.match(event.request)
//             .then(function (response) {
//                 return response || fetch(event.request);
//             })
//         );
//     }
// });
//
// function createCacheBustedRequest(url) {
//     const request = new Request(url, {cache: 'reload'});
//     // See https://fetch.spec.whatwg.org/#concept-request-mode
//     // This is not yet supported in Chrome as of M48, so we need to explicitly check to see
//     // if the cache: 'reload' option had any effect.
//     if ('cache' in request) {
//         return request;
//     }
//
//     // If {cache: 'reload'} didn't have any effect, append a cache-busting URL parameter instead.
//     const cacheBustingUrl = new URL(url, self.location.href);
//     cacheBustingUrl.search += (cacheBustingUrl.search ? '&' : '') + 'cachebust=' + Date.now();
//     return new Request(cacheBustingUrl);
// }
