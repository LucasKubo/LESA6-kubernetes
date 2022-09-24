var CACHE_NAME = 'v2';

const resourcesToPreCache = [
    '/',
    '/index',
    '/login',
    '/images/icon.svg',
    '/style/homeStyle.css',
    '/style/loginStyle.css',
]

self.addEventListener('install', function (event) {
    event.waitUntil(
        caches.open(CACHE_NAME).then(function (cache) {
            return cache.addAll(resourcesToPreCache);
        })
    )
});

self.addEventListener('fetch', function (event) {
    event.respondWith(
        caches.match(event.request).then(function (cachedResponse) {
            return cachedResponse || fetch(event.request);
        })
    );
});