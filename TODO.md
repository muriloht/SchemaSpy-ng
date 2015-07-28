To Do
=====

* Add QR codes with CSS when printed
```
@media print {
	headerHolder::before {
		content: url(http://chart.apis.google.com/chart?chs=120x120&cht=qr&chl=http%3A%2F%2Fexample.org/);
	}
}
```

* Remove objectsvg-imgpng thing, everyone will have to deal with SVG now (better for printing).  (Check if still true.)
