
const checkbox = document.querySelector('.dark-mode-checkbox');

// selected prefered theme
const userTheme = localStorage.getItem("theme");

// theme by the system
const systemTheme = window.matchMedia("(prefers-color-scheme: dark)").matches;

// check theme initialy
const themeCheck = () => {
	if (userTheme === "dark" || (!userTheme && systemTheme)) {
		document.documentElement.setAttribute('data-theme', 'dark');
		checkbox.setAttribute('checked', 'checked');
		return;
	}
	document.documentElement.setAttribute('data-theme', 'light')
	checkbox.removeAttribute('checked');
}

// switch theme manualy
const themeSwitch = () => {
	if (document.documentElement.getAttribute('data-theme') === "dark") {
		document.documentElement.setAttribute('data-theme', 'light')
		localStorage.setItem("theme", "light");
		return;
	}
	document.documentElement.setAttribute('data-theme', 'dark')
	localStorage.setItem("theme", "dark");
}

checkbox.addEventListener('change', () => {
	themeSwitch();
})

themeCheck();