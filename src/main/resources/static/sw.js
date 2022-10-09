var CACHE_NAME = 'v1';

const resourcesToPreCache = [
    //Páginas que não exigem autenticação de usuário

    //Login
//    '/',
//    '/login',
//    '/images/icon.svg',
//    '/style/homeStyle.css',
//    '/style/loginStyle.css',
//    '/script/login.jsp',
//
//    //Index
//    '/Index',
//    '/style/indexStyle.css',

//    //Cadastro
//    '/cadastro',
//    '/style/cadastroUsuarioStyle.css',
//    '/script/cadastroUsuario.jsp',

    //Páginas que exigem autenticação de usuário

    // //Home
    // '/home',
    // '/style/calendario1.css',
    // '/style/calendario2.css',
    // 'css/bootstrap.min.css',
    // 'css/style.css',
    // '/script/calendario4.js',
    //
    // //Cadastro Remedios
    // '/cadastro_remedio',
    // '/style/cadastroRemedioStyle.css',
    // '/style/bootstrap-multiselect.css',
    // '/script/cadastroRemedio.jsp',
    //
    // //Lista agendamento
    // '/lista_agendamento',


//    'https://fonts.googleapis.com/css?family=Roboto&display=swap',
//    'https://cdnjs.cloudflare.com/ajax/libs/malihu-custom-scrollbar-plugin/3.1.5/jquery.mCustomScrollbar.min.css',
//    'https://use.fontawesome.com/releases/v5.0.13/js/solid.js',
//    'https://use.fontawesome.com/releases/v5.0.13/js/fontawesome.js',
//
//    'https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js',
//    //Bootstrap
//    'https://code.jquery.com/jquery-3.3.1.slim.min.js',
//    'https://cdn.jsdelivr.net/npm/popper.js@1.14.7/dist/umd/popper.min.js',
//    'https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js',

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