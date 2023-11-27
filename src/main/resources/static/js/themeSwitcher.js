document.addEventListener('DOMContentLoaded', function () {
	const checkbox = document.querySelector('.dark-mode-checkbox');
	var htmlElement = document.querySelector('html');

	checkbox.addEventListener('change', function () {

		if (this.checked) {
			htmlElement.setAttribute('data-theme', 'dark');
		} else {
			htmlElement.setAttribute('data-theme', 'light');
		}
	})
});
