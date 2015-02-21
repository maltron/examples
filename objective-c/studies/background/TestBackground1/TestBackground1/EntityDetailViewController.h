//
//  EntityViewController.h
//  TeamPlayerCoreDataExample
//
//  Created by Mauricio Leal on 1/2/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>

@interface EntityDetailViewController : UIViewController

-(UILabel *)createLabel:(NSString *)text;
-(UITextField *)createTextField:(NSString *)text placeHolder:(NSString *)placeHolder;
-(CGFloat)calculateMaxWidth:(NSArray *)array;

@end
