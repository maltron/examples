//
//  TextFieldCell.h
//  TeamPlayerCoreDataExample
//
//  Created by Mauricio Leal on 1/2/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <UIKit/UIKit.h>

#define PADDING 16.0f

@interface TextFieldCell : UITableViewCell
@property (nonatomic, strong) UILabel *label;
@property (nonatomic, strong) UITextField *textField;
@property (nonatomic, assign) CGFloat maxWidth;

-(id)initWithLabel:(UILabel *)label andTextField:(UITextField *)textField withMaxWidth:(CGFloat)maxWidth reuseIdentifier:(NSString *)reuseIdentifer;

@end
