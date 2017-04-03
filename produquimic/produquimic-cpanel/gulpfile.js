var gulp = require('gulp'),
    usemin = require('gulp-usemin'),
    wrap = require('gulp-wrap'),
    connect = require('gulp-connect'),
    connectphp = require('gulp-connect-php'),
    watch = require('gulp-watch'),
    minifyCss = require('gulp-minify-css'),
    minifyJs = require('gulp-uglify'),
    concat = require('gulp-concat'),
    less = require('gulp-less'),
    rename = require('gulp-rename'),
    minifyHTML = require('gulp-minify-html');

var paths = {
    scripts: 'src/js/**/*.*',
    styles: 'src/less/**/*.*',
    images: 'src/img/**/*.*',
    views: 'src/views/**/*.{html,php}',
    framework: 'src/framework/**/*.*',
    Slim: 'src/Slim/**/*.*',
    index: 'src/*.{html,php}',
    bower_fonts: 'src/components/**/*.{ttf,woff,woff2,eof,svg}',
};

/**
 * Handle bower components from index
 */
gulp.task('usemin', function() {
    return gulp.src(paths.index)
        .pipe(usemin({
            js: [minifyJs(), 'concat'],
            css: [minifyCss({keepSpecialComments: 0}), 'concat'],
        }))
        .pipe(gulp.dest('dist/'));
});

/**
 * Copy assets
 */
gulp.task('build-assets', ['copy-bower_fonts']);

gulp.task('copy-bower_fonts', function() {
    return gulp.src(paths.bower_fonts)
        .pipe(rename({
            dirname: '/fonts'
        }))
        .pipe(gulp.dest('dist/lib'));
});

/**
 * Handle custom files
 */
gulp.task('build-custom', ['custom-images', 'custom-js', 'custom-less', 'custom-views', 'custom-framework', 'custom-Slim']);

gulp.task('custom-images', function() {
    return gulp.src(paths.images)
        .pipe(gulp.dest('dist/img'));
});

gulp.task('custom-js', function() {
    return gulp.src(paths.scripts)
        
        .pipe(concat('dashboard.min.js'))
        .pipe(gulp.dest('dist/js'));
});

gulp.task('custom-less', function() {
    return gulp.src(paths.styles)
        
        .pipe(gulp.dest('dist/css'));
});

gulp.task('custom-views', function() {
    return gulp.src(paths.views)
        .pipe(minifyHTML())
        .pipe(gulp.dest('dist/views'));
});

gulp.task('custom-framework', function() {
    return gulp.src(paths.framework)
        .pipe(minifyHTML())
        .pipe(gulp.dest('dist/framework'));
});


gulp.task('custom-Slim', function() {
    return gulp.src(paths.Slim)        
        .pipe(gulp.dest('dist/Slim'));
});



/**
 * Watch custom files
 */
gulp.task('watch', function() {
    gulp.watch([paths.images], ['custom-images']);
    gulp.watch([paths.styles], ['custom-less']);
    gulp.watch([paths.scripts], ['custom-js']);
    gulp.watch([paths.views], ['custom-views']);
    gulp.watch([paths.Slim], ['custom-Slim']);
    gulp.watch([paths.framework], ['custom-framework']);
    gulp.watch([paths.index], ['usemin']);
});

/**
 * Live reload server
 */
gulp.task('webserver', function() {
    connect.server({
        root: 'dist',
        livereload: true,
        port: 8888
    });
});

gulp.task('livereload', function() {
    gulp.src(['dist/**/*.*'])
        .pipe(watch())
        .pipe(connect.reload());
});

gulp.task('connect', function() {
    connectphp.server({
    hostname: '127.0.0.1',
    bin: 'C:/wamp/bin/php/php5.5.12/php.exe',
    ini: 'C:/wamp/bin/php/php5.5.12/php.ini',
    port: 8000,
    base: 'dist',
    livereload: true
  });
});

/**
 * Gulp tasks
 */
gulp.task('build', ['usemin', 'build-assets', 'build-custom']);
//PARA SERVIDOR 8888 NO PHP
//gulp.task('default', ['build', 'webserver', 'livereload', 'watch']);
gulp.task('default', ['build', 'connect', 'livereload', 'watch']);
