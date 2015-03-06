package com.techstar.modules.jcaptcha.engine;

import java.awt.Color;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator;
import com.octo.captcha.component.image.color.SingleColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.BaffleTextDecorator;
import com.octo.captcha.component.image.textpaster.textdecorator.LineTextDecorator;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

public class MyCaptchaEngineEx extends ListImageCaptchaEngine {

	protected void buildInitialFactories() {
		/**
		 * Set Captcha Word Length Limitation which should not over 6
		 */
		Integer minAcceptedWordLength = new Integer(4);
		Integer maxAcceptedWordLength = new Integer(5);
		/**
		 * Set up Captcha Image Size: Height and Width
		 */
		Integer imageHeight = new Integer(50);
		Integer imageWidth = new Integer(100);
		/**
		 * Set Captcha Font Size between 50 and 55
		 */
		Integer minFontSize = new Integer(20);
		Integer maxFontSize = new Integer(22);
		/**
		 * We just generate digit for captcha source char Although you can use
		 * abcdefg......xyz
		 */
		com.octo.captcha.component.word.wordgenerator.WordGenerator wordGenerator = (new RandomWordGenerator(
				"0123456789"));
		/**
		 * cyt and unruledboy proved that backgroup not a factor of Security. A
		 * captcha attacker won't affaid colorful backgroud, so we just use
		 * white color, like google and hotmail.
		 */
		BackgroundGenerator backgroundGenerator = new GradientBackgroundGenerator(imageWidth,
				imageHeight, Color.white, Color.white);
		/**
		 * font is not helpful for security but it really increase difficultness
		 * for attacker
		 */
		FontGenerator _fontGenerator = new RandomFontGenerator(minFontSize, maxFontSize);
		/**
		 * Note that our captcha color is Blue
		 */
		SingleColorGenerator scg = new SingleColorGenerator(Color.blue);
		/**
		 * decorator is very useful pretend captcha attack. we use two line text
		 * decorators.
		 */
		LineTextDecorator line_decorator = new LineTextDecorator(1, Color.blue);
		// LineTextDecorator line_decorator2 = new LineTextDecorator(1,
		// Color.blue);
		TextDecorator[] textdecorators = new TextDecorator[1];

		textdecorators[0] = line_decorator;
		// textdecorators[1] = line_decorator2;

		TextPaster _textPaster = new DecoratedRandomTextPaster(minAcceptedWordLength,
				maxAcceptedWordLength, scg, new TextDecorator[] { new BaffleTextDecorator(
						new Integer(1), Color.white) });

		/**
		 * ok, generate the WordToImage Object for logon service to use.
		 */
		com.octo.captcha.component.image.wordtoimage.WordToImage wordToImage = new ComposedWordToImage(
				_fontGenerator, backgroundGenerator, _textPaster);
		addFactory(new GimpyFactory(wordGenerator, wordToImage));
	}

}
