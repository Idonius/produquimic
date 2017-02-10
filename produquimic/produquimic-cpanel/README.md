# jdframework
## Responsive Angular, bloat free, bootstrap powered AdminLTE Bootstrap Template!
[![Gitter](https://badges.gitter.im/Join Chat.svg)](https://gitter.im/rdash/rdash-angular?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Angularjs based framework for Web Application Development.

Check out the live example!

## Usage
### Requirements
* [NodeJS](http://nodejs.org/) (with [NPM](https://www.npmjs.org/))
* [Bower](http://bower.io)
* [Gulp](http://gulpjs.com)

### Installation
1. Clone the repository: `https://github.com/diego10j/jdframework.git`
2. Install the NodeJS dependencies: `sudo npm install`.
3. Install the Bower dependencies: `bower install`.
4. Run the gulp build task: `gulp build`.
5. Run the gulp default task: `gulp`. This will build any changes made automatically, and also run a live reload server on [http://localhost:8000](http://localhost:8000).

Ensure your preferred web server points towards the `dist` directory.

### Development
Continue developing the jdframework further by editing the `src` directory. With the `gulp` command, any file changes made will automatically be compiled into the specific location within the `dist` directory.

#### Modules & Packages
By default, jdframework includes [`ui.bootstrap`](http://angular-ui.github.io/bootstrap/), [`ui.router`](https://github.com/angular-ui/ui-router) and [`ngCookies`](https://docs.angularjs.org/api/ngCookies). 

If you'd like to include any additional modules/packages not included with jdframework, add them to your `bower.json` file and then update the `src/index.html` file, to include them in the minified distribution output.

## Credits
* [Diego Jacome](https://github.com/diego10j)
