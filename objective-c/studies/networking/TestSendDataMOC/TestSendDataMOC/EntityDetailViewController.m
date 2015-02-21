//
//  EntityViewController.m
//  TeamPlayerCoreDataExample
//
//  Created by Mauricio Leal on 1/2/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "EntityDetailViewController.h"
#import "AutoLayout.h"

@interface EntityDetailViewController()
@property (nonatomic, strong) UITableView *tableView;

@end

@implementation EntityDetailViewController
@synthesize tableView = _tableView;

-(void)loadView {
    [super loadView];
    
    _tableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStyleGrouped];
    [self.tableView setTranslatesAutoresizingMaskIntoConstraints:NO];
    // IMPORTANT: It's imperative child classes implements UITableViewDataSource
    [self.tableView setDataSource:self];
    // IMPORTANT: It's imperative child classes implements UITableViewDelegate
    [self.tableView setDelegate:self];
    [self.view addSubview:self.tableView];
    [self.view addConstraints:[AutoLayout fill:self.tableView inside:self.view]];
}

-(UILabel *)createLabel:(NSString *)text {
    UILabel *label = [[UILabel alloc] init];
    if(text) [label setText:text];
    [label setTextAlignment:NSTextAlignmentRight];
    [label setBackgroundColor:[UIColor clearColor]];
    
    NSString *familyName = [[label font] familyName];
    NSArray *fontNames = [UIFont fontNamesForFamilyName:familyName];
    for(NSString *fontName in fontNames)
        if([fontName rangeOfString:@"bold" options:NSCaseInsensitiveSearch].location != NSNotFound) {
            UIFont *bold = [UIFont fontWithName:fontName size:[label font].pointSize];
            [label setFont:bold];
        }
    
    return label;
}

-(UITextField *)createTextField:(NSString *)text placeHolder:(NSString *)placeHolder {
    UITextField *textField = [[UITextField alloc] init];
    if(text) [textField setText:text];
    if(placeHolder) [textField setPlaceholder:placeHolder];
    [textField setAutocorrectionType:UITextAutocorrectionTypeNo];
    [textField setAutocapitalizationType:UITextAutocapitalizationTypeNone];
    [textField setAdjustsFontSizeToFitWidth:NO];
    [textField setClearButtonMode:UITextFieldViewModeWhileEditing];
    [textField setBorderStyle:UITextBorderStyleNone];
    [textField setReturnKeyType:UIReturnKeyDefault];
    [textField setTextAlignment:NSTextAlignmentLeft];
    [textField setAutocapitalizationType:UITextAutocapitalizationTypeNone];
    
    
    return textField;
}

-(CGFloat)calculateMaxWidth:(NSArray *)array {
    CGFloat max = 0;
    for(UILabel *label in array) {
        [label sizeToFit];
        if(label.frame.size.width > max)
            max = label.frame.size.width;
    }
    
    return max;
}

@end
